package com.unipi.iason.erg2v3;
import com.unipi.iason.Product;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

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


class MiningTask extends Thread {
    private int startN;
    private int endN;
    private int prefix;
    private String prefixString;
    private ReentrantLock lock;
    private CountDownLatch latch;

    public MiningTask(int startN, int endN, int prefix, ReentrantLock lock, CountDownLatch latch) {
        this.startN = startN;
        this.endN = endN;
        this.prefix = prefix;
        this.prefixString = new String(new char[prefix]).replace('\0', '0');
        this.lock = lock;
        this.latch = latch;
    }

    @Override
    public void run() {
        for (int num = startN; num < endN; num++) {
            n = num;
            String hash = calculateBlockHash();
            if (hash.substring(0, prefix).equals(prefixString)) {
                // If a valid hash is found, set it and return
                lock.lock();
                try {
                    Block.this.hash = hash;
                } finally {
                    lock.unlock();
                }
                latch.countDown();
                return;
            }
        }
        latch.countDown();
    }
}

public void mineBlock(int prefix) {
    int numThreads = Runtime.getRuntime().availableProcessors();
    Thread[] threads = new Thread[numThreads];
    ReentrantLock lock = new ReentrantLock();
    CountDownLatch latch = new CountDownLatch(numThreads);

    int nRange = Integer.MAX_VALUE / numThreads;
    for (int i = 0; i < numThreads; i++) {
        int startN = i * nRange;
        int endN = (i + 1) * nRange;
        threads[i] = new MiningTask(startN, endN, prefix, lock, latch);
        threads[i].start();
    }

    try {
        latch.await();
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
}
}
