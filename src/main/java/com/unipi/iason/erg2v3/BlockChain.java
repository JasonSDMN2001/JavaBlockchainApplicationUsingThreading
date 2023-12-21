package com.unipi.iason.erg2v3;
import com.unipi.iason.Product;

import java.util.ArrayList;
import java.util.List;
public class BlockChain {
    public List<Block> blocks;
    public BlockChain() {
        blocks = new ArrayList<>();
    }

    public void addBlock(Block block) {
        if (blocks.isEmpty()) {
            blocks.add(block);
        } else {
            Block previousBlock = blocks.get(blocks.size() - 1);
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

    public List<Product> searchProduct(String criteria) {
        List<Product> foundProducts = new ArrayList<>();
        for (Block block : blocks) {
            for (Product product : block.getProducts()) {
                if (product.getTitle().contains(criteria)) {
                    foundProducts.add(product);
                }
            }
        }
        return foundProducts;
    }

    public List<Product> getProductStatistics(int productId) {
        List<Product> statistics = new ArrayList<>();
        for (Block block : blocks) {
            for (Product product : block.getProducts()) {
                if (product.getId() == productId) {
                    statistics.add(product);
                }
            }
        }
        return statistics;
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
