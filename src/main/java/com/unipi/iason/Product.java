package com.unipi.iason;
import com.unipi.iason.erg2v2.Main;

import java.sql.Timestamp;
public class Product {
    private int id;
    private String productCode;
    private String title;
    private long timestamp;
    private double price;
    private String description;
    private String category;
    private int previousProductId;
    //constructor with builder api
    public Product(Builder builder) {
        //αυξοντας αριθμος εγγραφης
        this.id = builder.id;
        this.productCode = builder.productCode;
        this.title = builder.title;
        this.timestamp = builder.timestamp;
        this.price = builder.price;
        this.description = builder.description;
        this.category = builder.category;
        this.previousProductId = builder.previousProductId;
    }
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getProductCode() {
        return productCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getPreviousProductId() {
        return previousProductId;
    }

    public static class Builder {
        private final int id;

        private String productCode = ""; //strings dont have a default value
        private String title = "";
        private long timestamp = Main.timeStamp;
        private double price = 0.0;
        private String description = "";
        private String category = "";
        private int previousProductId = 0;

        public Builder(int id) {
            this.id = id;
        }

        public Builder productCode(String productCode) {
            this.productCode = productCode;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder previousProductId(int previousProductId) {
            this.previousProductId = previousProductId;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
