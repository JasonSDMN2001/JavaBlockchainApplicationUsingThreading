package com.unipi.iason.erg2v1;
import com.unipi.iason.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BlockChain {
    public List<Block> blocks;
    public BlockChain() {
        blocks = new ArrayList<>();
    }

    //can add multiple products from the beginning
    public void addBlock(Block block) {
        if (blocks.isEmpty()) {
            blocks.add(block);
        } /*else {
            Block previousBlock = blocks.get(blocks.size() - 1);
            blocks.add(block);
        }*/else {
            Block previousBlock = blocks.get(blocks.size() - 1);
            List<Product> previousProducts = previousBlock.getProducts();
            List<Product> currentProducts = block.getProducts();

            //checking for id's to be ascending
            for (int i = 0; i < currentProducts.size(); i++) {
                if (i > 0 && currentProducts.get(i).getId() <= currentProducts.get(i - 1).getId()) {
                    System.out.println("cant add block cause products dont have ascending id's");
                    return;
                }
                if (!previousProducts.isEmpty() && currentProducts.get(i).getId() <= previousProducts.get(previousProducts.size() - 1).getId()) {
                    System.out.println("cant add block cause products dont have ascending id's");
                    return;
                }
            }
            blocks.add(block);
        }
    }


    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        for (Block block : blocks) {
            allProducts.addAll(block.getProducts());
        }
        return allProducts;
    }
    //multiple characteristics and multiple times
    public List<Product> searchProduct(String criteria) {
        List<Product> foundProducts = new ArrayList<>();
        for (Block block : blocks) {
            for (Product product : block.getProducts()) {
                if (product.getTitle().contains(criteria)) {
                    foundProducts.add(product);
                }else if(product.getCategory().contains(criteria)){
                    foundProducts.add(product);
                }else if(product.getDescription().contains(criteria)){
                    foundProducts.add(product);
                }
            }
        }
        return foundProducts;
    }
    // could take each product's price and timestamp,and make a graph
    public void getProductStatistics(String title) {
        for (Block block : blocks) {
            for (Product product : block.getProducts()) {
                if (Objects.equals(product.getTitle(), title)) {
                    System.out.println("Timestamp: " + new Date( product.getTimestamp()));
                    System.out.println("Product Price: " + product.getPrice());
                }
            }
        }
    }
    public static boolean isChainValid(List<Block> blocks, int prefix){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[prefix]).replace('\0','0');
        for (int i=1;i<blocks.size();i++){
            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i-1);
            String s = currentBlock.calculateBlockHash();
            if (!currentBlock.getHash().equals(s)){
                System.out.println("Current Hashes not equal");
                return false;}
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())){
                System.out.println("Previous Hashes not equal");
                return false;}
            if (!currentBlock.getHash().substring(0,prefix).equals(hashTarget)){
                System.out.println("This block hasn't been mined");
                return false;}
        }
        return true;
    }
}
