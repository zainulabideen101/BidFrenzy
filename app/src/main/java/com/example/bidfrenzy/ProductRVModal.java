package com.example.bidfrenzy;

public class ProductRVModal {
    private String productName;
    private String productPrice;
    private String productCategory;
    private String productImage;
    private String productDescription;
    private String productID;

    public ProductRVModal(){

    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }


    public ProductRVModal(String productName, String productPrice, String productCategory, String productImage, String productDescription, String productID) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productID = productID;
    }

}
