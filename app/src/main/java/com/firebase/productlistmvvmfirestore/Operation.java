package com.firebase.productlistmvvmfirestore;

class Operation {
    Product product;
    int type;

    Operation(Product product, int type) {
        this.product = product;
        this.type = type;
    }
}