// package com.example.orderservice.entity;

// import javax.persistence.*;
// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;

// @Entity
// @Table(name = "orders")
// public class Order {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private Long userId;
//     private String orderNo;
//     private BigDecimal total;
//     private String status; // pending, paid, cancelled
//     private LocalDateTime createdAt = LocalDateTime.now();

//     @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//     private List<OrderItem> items = new ArrayList<>();

//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }
//     public Long getUserId() { return userId; }
//     public void setUserId(Long userId) { this.userId = userId; }
//     public String getOrderNo() { return orderNo; }
//     public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
//     public BigDecimal getTotal() { return total; }
//     public void setTotal(BigDecimal total) { this.total = total; }
//     public String getStatus() { return status; }
//     public void setStatus(String status) { this.status = status; }
//     public LocalDateTime getCreatedAt() { return createdAt; }
//     public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
//     public List<OrderItem> getItems() { return items; }
//     public void setItems(List<OrderItem> items) { this.items = items; }
// }
package com.example.orderservice.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    
    private BigDecimal totalAmount;
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getReceiverPhone() { return receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    public String getReceiverAddress() { return receiverAddress; }
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}