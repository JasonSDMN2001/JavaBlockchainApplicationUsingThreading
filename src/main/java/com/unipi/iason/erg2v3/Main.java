package com.unipi.iason.erg2v3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unipi.iason.DBConnection;
import com.unipi.iason.Product;
import com.unipi.iason.erg2v3.Block;
import com.unipi.iason.erg2v3.BlockChain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Main {
    public static final int prefix = 1;
    public static final long timeStamp = new Date().getTime();
    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
        List<Product> products = new ArrayList<>();
        Product p1 = new Product.Builder(1)
                .productCode("1234")
                .title("Title1")
                .timestamp(timeStamp)
                .price(10.0).
                description("Description1")
                .category("Category1")
                .previousProductId(0)
                .build();
        Product p2 = new Product.Builder(2)
                .productCode("1235")
                .title("Title2")
                .timestamp(timeStamp)
                .price(20.0).
                description("Description2")
                .category("Category2")
                .previousProductId(1)
                .build();
        products.add(p1);
        products.add(p2);
        // System.out.println("Process started");
        Block genesisBlock = new Block("0", products,
                timeStamp);
        long startTime = System.currentTimeMillis();
        genesisBlock.mineBlock(prefix);
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        double timeInMinutes = (double) timeElapsed / 60000;
        System.out.println("Block 1 Execution time in minutes: " + timeInMinutes);
        blockChain.addBlock(genesisBlock);
        //System.out.println("Node " + (blockChain.blocks.size()) + " created");
        List<Product> products2 = new ArrayList<>();
        Product p3 = new Product.Builder(3)
                .productCode("1236")
                .title("Title3")
                .timestamp(timeStamp)
                .price(30.0).
                description("Description3")
                .category("Category3")
                .previousProductId(2)
                .build();
        products2.add(p3);
        //2nd Block
        Block secondBlock = new Block(blockChain.blocks.get(blockChain.blocks.size() - 1).getHash(),
                products2,
                timeStamp);
        long startTime2 = System.currentTimeMillis();
        secondBlock.mineBlock(prefix);
        long endTime2 = System.currentTimeMillis();
        long timeElapsed2 = endTime2 - startTime2;
        double timeInMinutes2 = (double) timeElapsed2 / 60000;
        System.out.println("Block 2 Execution time in minutes: " + timeInMinutes2);
        blockChain.addBlock(secondBlock);
        // System.out.println("Node " + (blockChain.blocks.size()) + " created!");
        //3rd Block
        List<Product> products3 = new ArrayList<>();
        Product p4 = new Product.Builder(4)
                .productCode("1237")
                .title("Title4")
                .timestamp(timeStamp)
                .price(40.0).
                description("Description4")
                .category("Category4")
                .previousProductId(3)
                .build();
        products3.add(p4);
        Block thirdBlock = new Block(blockChain.blocks.get(blockChain.blocks.size() - 1).getHash(),
                products3,
                timeStamp);
        long startTime3 = System.currentTimeMillis();
        thirdBlock.mineBlock(prefix);
        long endTime3 = System.currentTimeMillis();
        long timeElapsed3 = endTime3 - startTime3;
        double timeInMinutes3 = (double) timeElapsed3 / 60000;
        System.out.println("Block 3 Execution time in minutes: " + timeInMinutes3);
        blockChain.addBlock(thirdBlock);
        //System.out.println("Node " + (blockChain.blocks.size()) + " created!");
        /*if(!BlockChain.isChainValid(blockChain.blocks, prefix))
            return;*/
        //Transform into Json
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        /*System.out.println("The blockChain:");
        System.out.println(json);*/
        DBConnection dbConnection = new DBConnection();
        //dbConnection.createTableAndData();
        dbConnection.insertNewProduct(1, json);
        String results = dbConnection.selectAll(1);
        /*System.out.println(json.length());
        System.out.println(results.length());*/
        BlockChain blockChain2 = null;
        if (results != null) {
            blockChain2 = new Gson().fromJson(results.trim(), BlockChain.class);
            String json4 = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain2);
            //System.out.println("The 2nd blockChain:");
            //System.out.println(json.trim());
            //System.out.println(json4.trim());
           /* System.out.println(json.hashCode());
            System.out.println(json4.hashCode());*/
            // System.out.println(json.equals(json4));
            /*System.out.println("Is chain valid? Result: " + BlockChain.isChainValid(blockChain.blocks, prefix));
            System.out.println("Is chain2 valid? Result: " + BlockChain.isChainValid(blockChain2.blocks, prefix));*/
        }

        String json2 = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain2.getAllProducts());
        System.out.println("All products: ");
        System.out.println(json2);

        String json3 = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain2.searchProduct("Title1"));
        System.out.println("Search for: ");
        System.out.println(json3);
    }
}