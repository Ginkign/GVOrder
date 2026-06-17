// package com.example.productservice.controller;

// import com.example.productservice.dto.InventoryRequest;
// import com.example.productservice.entity.Product;
// import com.example.productservice.service.ProductService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.net.URI;
// import java.util.List;

// @RestController
// @RequestMapping("/api")
// public class ProductController {
//     private final ProductService productService;

//     public ProductController(ProductService productService) {
//         this.productService = productService;
//     }

//     @GetMapping("/products")
//     public ResponseEntity<List<Product>> list() {
//         return ResponseEntity.ok(productService.listAll());
//     }

//     @PostMapping("/products")
//     public ResponseEntity<Product> create(@RequestBody Product p) {
//         Product created = productService.create(p);
//         return ResponseEntity.created(URI.create("/api/products/" + created.getId())).body(created);
//     }

//     @PostMapping("/inventory/deduct")
//     public ResponseEntity<?> deduct(@RequestBody InventoryRequest req) {
//         boolean ok = productService.deductStock(req.getProductId(), req.getQuantity());
//         if (ok) return ResponseEntity.ok("deducted");
//         return ResponseEntity.badRequest().body("库存不足或商品不存在");
//     }

//     @PostMapping("/inventory/rollback")
//     public ResponseEntity<?> rollback(@RequestBody InventoryRequest req) {
//         productService.rollbackStock(req.getProductId(), req.getQuantity());
//         return ResponseEntity.ok("rolled back");
//     }
// }
package com.example.productservice.controller;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductRequest req) {
        Product p = productService.createProduct(req);
        return ResponseEntity.created(URI.create("/api/products/" + p.getId())).body(p);
    }

    @GetMapping
    public List<Product> list() {
        return productService.listProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // 内部接口：供订单服务调用扣减库存
    @PostMapping("/{id}/deduct-stock")
    public ResponseEntity<?> deductStock(@PathVariable Long id, @RequestParam int quantity) {
        boolean success = productService.deductStock(id, quantity);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("库存不足或商品不存在");
        }
    }
}