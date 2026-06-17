// package com.example.orderservice.entity;

// import javax.persistence.*;
// import java.math.BigDecimal;

// @Entity
// @Table(name = "order_items")
// public class OrderItem {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "order_id")
//     private Order order;

//     private Long productId;
//     private Integer quantity;
//     private BigDecimal price;

//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }
//     public Order getOrder() { return order; }
//     public void setOrder(Order order) { this.order = order; }
//     public Long getProductId() { return productId; }
//     public void setProductId(Long productId) { this.productId = productId; }
//     public Integer getQuantity() { return quantity; }
//     public void setQuantity(Integer quantity) { this.quantity = quantity; }
//     public BigDecimal getPrice() { return price; }
//     public void setPrice(BigDecimal price) { this.price = price; }
// }
package com.example.orderservice.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}