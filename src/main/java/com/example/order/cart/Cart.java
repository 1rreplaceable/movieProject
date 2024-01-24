package com.example.order.cart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class Cart {
    private Long productId;
    private String imagePath;
    private String productName;
    private String productDetail;
    private Integer quantity;
    private Integer productPrice;
    @Id
    private Long id;

    // 생성자
    public Cart(Long productId, String imagePath, String productName, String productDetail, Integer quantity, Integer productPrice) {
        this.productId = productId != null ? productId : 0L;
        this.imagePath = imagePath != null ? imagePath : "";
        this.productName = productName != null ? productName : "";
        this.productDetail = productDetail != null ? productDetail : "";
        this.quantity = quantity != null ? quantity : 0;
        this.productPrice = productPrice != null ? productPrice : 0;
    }

    public Cart() {

    }

    public void setId(Long id) {
        this.id = id;
    }

}
