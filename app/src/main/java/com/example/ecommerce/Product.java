package com.example.ecommerce;

import java.io.Serializable;

public class Product implements Serializable {
    String id;
    String name;
    String price;
    String quantity;
    String img;
    int drawable;
    String num_of_repeat;
    String shopcart_id;

    public Product(String name, String price, String quantity, String img,String id,int drawable,String num_of_repeat,String shopcart_id) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.img = img;
        this.id=id;
        this.drawable=drawable;
        this.num_of_repeat=num_of_repeat;
        this.shopcart_id=shopcart_id;
    }
}
