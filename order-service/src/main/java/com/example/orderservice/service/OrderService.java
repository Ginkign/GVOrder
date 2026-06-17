// package com.example.orderservice.service;

// import com.example.orderservice.dto.CreateOrderRequest;
// import com.example.orderservice.dto.OrderItemRequest;
// import com.example.orderservice.entity.Order;
// import com.example.orderservice.entity.OrderItem;
// import com.example.orderservice.repository.OrderRepository;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.client.RestTemplate;

// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.List;

// @Service
// public class OrderService {
//     private final OrderRepository orderRepository;
//     private final RestTemplate restTemplate;

//     @Value("${service.product.url:http://localhost:8082}")
//     private String productServiceUrl;

//     @Value("${service.user.url:http://localhost:8081}")
//     private String userServiceUrl;

//     public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
//         this.orderRepository = orderRepository;
//         this.restTemplate = restTemplate;
//     }

//     @Transactional
//     public Order createOrder(CreateOrderRequest req) {
//         // 1. verify user exists
//         String userUrl = userServiceUrl + "/api/users/" + req.getUserId();
//         try {
//             restTemplate.getForObject(userUrl, Object.class);
//         } catch (Exception e) {
//             throw new IllegalArgumentException("用户不存在或服务不可用");
//         }

//         List<OrderItem> savedItems = new ArrayList<>();
//         List<Long> deductedProducts = new ArrayList<>();

//         // 2. deduct stock for each item
//         for (OrderItemRequest it : req.getItems()) {
//             String deductUrl = productServiceUrl + "/api/inventory/deduct";
//             try {
//                 var body = java.util.Map.of("productId", it.getProductId(), "quantity", it.getQuantity());
//                 var resp = restTemplate.postForObject(deductUrl, body, String.class);
//                 // assume success if no exception
//                 deductedProducts.add(it.getProductId());
//                 OrderItem oi = new OrderItem();
//                 oi.setProductId(it.getProductId());
//                 oi.setQuantity(it.getQuantity());
//                 oi.setPrice(BigDecimal.ZERO);
//                 savedItems.add(oi);
//             } catch (Exception e) {
//                 // rollback previous deductions
//                 for (OrderItemRequest prev : req.getItems()) {
//                     if (deductedProducts.contains(prev.getProductId())) {
//                         try {
//                             var rb = java.util.Map.of("productId", prev.getProductId(), "quantity", prev.getQuantity());
//                             restTemplate.postForObject(productServiceUrl + "/api/inventory/rollback", rb, String.class);
//                         } catch (Exception ex) { /* log and continue */ }
//                     }
//                     if (prev.getProductId().equals(it.getProductId())) break;
//                 }
//                 throw new IllegalStateException("扣库存失败: " + e.getMessage());
//             }
//         }

//         // 3. create order
//         Order order = new Order();
//         order.setUserId(req.getUserId());
//         order.setStatus("pending");
//         order.setTotal(BigDecimal.ZERO);
//         for (OrderItem oi : savedItems) {
//             oi.setOrder(order);
//         }
//         order.setItems(savedItems);

//         Order saved;
//         try {
//             saved = orderRepository.save(order);
//         } catch (Exception e) {
//             // try rollback deducted stock
//             for (OrderItemRequest prev : req.getItems()) {
//                 try {
//                     var rb = java.util.Map.of("productId", prev.getProductId(), "quantity", prev.getQuantity());
//                     restTemplate.postForObject(productServiceUrl + "/api/inventory/rollback", rb, String.class);
//                 } catch (Exception ex) { /* log */ }
//             }
//             throw new IllegalStateException("保存订单失败");
//         }

//         return saved;
//     }

//     public Order findById(Long id) {
//         return orderRepository.findById(id).orElse(null);
//     }

//     @Transactional
//     public void payOrder(Long orderId) {
//         Order o = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("订单未找到"));
//         if (!"pending".equals(o.getStatus())) throw new IllegalStateException("订单不可支付");
//         o.setStatus("paid");
//         orderRepository.save(o);
//     }
// }
package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${service.user.url}")
    private String userServiceUrl;

    @Value("${service.product.url}")
    private String productServiceUrl;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest req) {
        // 1. 验证用户并获取地址信息 (调用 User Service)
        // GET /api/users/{id}/addresses/{addressId} - 假设我们有一个接口或者直接获取用户所有地址后筛选
        // 为简化，这里假设直接调用用户服务获取地址详情，实际项目中可能需要更细致的API
        Map<String, Object> addressInfo = null;
        try {
             // 注意：实际 user-service 需要提供获取单个地址的接口，这里简化处理，假设我们能拿到
             // 如果 user-service 没有单独获取地址接口，可以先 getUser 再在内存中找，或者新增接口
             // 这里为了演示，假设 user-service 有 /api/users/{userId}/addresses/{addressId} 
             // 但根据现有代码，只有 listAddresses。所以我们先 list 然后本地过滤，或者假设已有接口
             
             // 修正：根据现有 UserController，只有 listAddresses。
             List<Map<String, Object>> addresses = restTemplate.getForObject(
                     userServiceUrl + "/api/users/" + req.getUserId() + "/addresses", List.class);
             
             if (addresses == null || addresses.isEmpty()) {
                 throw new IllegalArgumentException("用户无地址信息");
             }
             
             // 简单查找匹配的地址ID
             Map<String, Object> targetAddr = addresses.stream()
                     .filter(a -> ((Number)a.get("id")).longValue() == req.getAddressId())
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("地址不存在"));

             addressInfo = targetAddr;

        } catch (Exception e) {
            throw new IllegalArgumentException("无法获取用户地址: " + e.getMessage());
        }

        // 2. 创建订单对象
        Order order = new Order();
        order.setUserId(req.getUserId());
        order.setReceiverName((String) addressInfo.get("receiver"));
        order.setReceiverPhone((String) addressInfo.get("phone"));
        order.setReceiverAddress((String) addressInfo.get("addressLine") + ", " + addressInfo.get("city"));
        order.setCreateTime(LocalDateTime.now());
        
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. 处理每个商品项：检查库存、扣减库存、计算价格
        for (CreateOrderRequest.OrderItemRequest itemReq : req.getItems()) {
            // 获取商品信息 (调用 Product Service)
            Map<String, Object> product = restTemplate.getForObject(
                    productServiceUrl + "/api/products/" + itemReq.getProductId(), Map.class);
            
            if (product == null) {
                throw new IllegalArgumentException("商品不存在: " + itemReq.getProductId());
            }

            // 扣减库存 (调用 Product Service)
            Boolean deductSuccess = restTemplate.postForObject(
                    productServiceUrl + "/api/products/" + itemReq.getProductId() + "/deduct-stock?quantity=" + itemReq.getQuantity(),
                    null, Boolean.class);
            
            if (Boolean.FALSE.equals(deductSuccess)) {
                throw new IllegalArgumentException("商品库存不足: " + product.get("name"));
            }

            // 构建订单项
            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setProductName((String) product.get("name"));
            item.setPrice(new BigDecimal(product.get("price").toString()));
            item.setQuantity(itemReq.getQuantity());
            item.setOrder(order);
            
            orderItems.add(item);
            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    public List<Order> listOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}