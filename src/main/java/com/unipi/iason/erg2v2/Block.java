package com.unipi.iason.erg2v2;
import com.unipi.iason.Product;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Block {
    //the data
    private List<Product> products;
    private String previousHash;
    private long timestamp;
    private String hash;
    private long timeStamp;
    private int n;
    public Block(String previousHash, List<Product> data, long timeStamp) {
        this.previousHash = previousHash;
        this.products = data;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
    }

    public String calculateBlockHash(){
        String dataToHash = previousHash+String.valueOf(timeStamp)
                +String.valueOf(products)+String.valueOf(n);
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes){
            builder.append(String.format("%02x",b));
        }
        return builder.toString();
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
    class MiningTask implements Callable<String> {
        private int startNonce;
        private int endNonce;
        private int prefix;
        private String prefixString;

        public MiningTask(int startNonce, int endNonce, int prefix) {
            this.startNonce = startNonce;
            this.endNonce = endNonce;
            this.prefix = prefix;
            this.prefixString = new String(new char[prefix]).replace('\0', '0');
        }

        @Override
        public String call() {
            for (int nonce = startNonce; nonce < endNonce; nonce++) {
                n = nonce;
                String hash = calculateBlockHash();
                if (hash.substring(0, prefix).equals(prefixString)) {
                    return hash;
                }
            }
            return null;
        }
    }

    public String mineBlock(int prefix) throws InterruptedException, ExecutionException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<String>> futures = new ArrayList<>();

        int nonceRange = Integer.MAX_VALUE / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int startNonce = i * nonceRange;
            int endNonce = (i + 1) * nonceRange;
            futures.add(executor.submit(new MiningTask(startNonce, endNonce, prefix)));
        }

        String validHash = null;
        for (Future<String> future : futures) {
            validHash = future.get();
            if (validHash != null) {
                break;
            }
        }

        executor.shutdownNow();
        return validHash;
    }
}
