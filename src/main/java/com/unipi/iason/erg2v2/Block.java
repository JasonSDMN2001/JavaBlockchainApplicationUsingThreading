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
    class MiningTask implements Runnable {
        private int startN;
        private int endN;
        private int prefix;
        private String prefixString;

        public MiningTask(int startN, int endN, int prefix) {
            this.startN = startN;
            this.endN = endN;
            this.prefix = prefix;
            this.prefixString = new String(new char[prefix]).replace('\0', '0');
        }
        //on run we will try to find the hash
        @Override
        public void run() {
            for (int num = startN; num < endN; num++) {
                n = num;
                String hash = calculateBlockHash();
                if (hash.substring(0, prefix).equals(prefixString)) {
                    synchronized (Block.this) { //lockin.g the block using synchronized
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

        int nRange = Integer.MAX_VALUE / numThreads; //give each thread a range of n,equally
        for (int i = 0; i < numThreads; i++) {
            int startN = i * nRange;
            int endN = (i + 1) * nRange;
            threads[i] = new Thread(new MiningTask(startN, endN, prefix));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join(); //wait for all threads to finish
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
