package com.unipi.iason.erg2v3;
import com.unipi.iason.Product;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Block {
    //the data
    private List<Product> products;
    private String previousHash;
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

   /* public String mineBlock(int prefix){
        String prefixString = new String(new char[prefix]).replace('\0','0');
        while (!hash.substring(0,prefix).equals(prefixString)){
            n++;
            hash = calculateBlockHash();
        }
        return hash;
    }*/

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

    class MiningTask implements Runnable {
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
        public void run() {
            for (int num = startNonce; num < endNonce; num++) {
                n = num;
                String hash = calculateBlockHash();
                if (hash.substring(0, prefix).equals(prefixString)) {
                    // If a valid hash is found, set it and return
                    synchronized (Block.this) {
                        Block.this.hash = hash;
                    }
                    return;
                }
            }
        }
    }

    public void mineBlock(int prefix) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numThreads];

        int nRange = Integer.MAX_VALUE / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int startN = i * nRange;
            int endN = (i + 1) * nRange;
            threads[i] = new Thread(new MiningTask(startN, endN, prefix));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
