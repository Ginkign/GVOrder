// package com.example.orderservice.controller;

// import com.example.orderservice.dto.CreateOrderRequest;
// import com.example.orderservice.entity.Order;
// import com.example.orderservice.service.OrderService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.net.URI;

// @RestController
// @RequestMapping("/api/orders")
// public class OrderController {
//     private final OrderService orderService;

//     public OrderController(OrderService orderService) {
//         this.orderService = orderService;
//     }

//     @PostMapping
//     public ResponseEntity<?> create(@RequestBody CreateOrderRequest req) {
//         try {
//             Order o = orderService.createOrder(req);
//             return ResponseEntity.created(URI.create("/api/orders/" + o.getId())).body(o);
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         } catch (Exception e) {
//             return ResponseEntity.status(500).body(e.getMessage());
//         }
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<?> get(@PathVariable Long id) {
//         Order o = orderService.findById(id);
//         if (o == null) return ResponseEntity.notFound().build();
//         return ResponseEntity.ok(o);
//     }

//     @PostMapping("/{id}/pay")
//     public ResponseEntity<?> pay(@PathVariable Long id) {
//         try {
//             orderService.payOrder(id);
//             return ResponseEntity.ok("paid");
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         } catch (Exception e) {
//             return ResponseEntity.status(500).body(e.getMessage());
//         }
//     }
// }
package com.example.orderservice.controller;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest req) {
        try {
            Order order = orderService.createOrder(req);
            return ResponseEntity.created(URI.create("/api/orders/" + order.getId())).body(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public List<Order> listUserOrders(@PathVariable Long userId) {
        return orderService.listOrdersByUser(userId);
    }
}