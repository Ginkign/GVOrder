// package com.example.productservice.service;

// import com.example.productservice.entity.Product;
// import com.example.productservice.repository.ProductRepository;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class ProductService {
//     private final ProductRepository productRepository;

//     public ProductService(ProductRepository productRepository) {
//         this.productRepository = productRepository;
//     }

//     public List<Product> listAll() { return productRepository.findAll(); }

//     public Product create(Product p) { return productRepository.save(p); }

//     public Optional<Product> findById(Long id) { return productRepository.findById(id); }

//     @Transactional
//     public synchronized boolean deductStock(Long productId, int qty) {
//         Product p = productRepository.findById(productId).orElse(null);
//         if (p == null) return false;
//         if (p.getStock() == null || p.getStock() < qty) return false;
//         p.setStock(p.getStock() - qty);
//         productRepository.save(p);
//         return true;
//     }

//     @Transactional
//     public synchronized void rollbackStock(Long productId, int qty) {
//         Product p = productRepository.findById(productId).orElse(null);
//         if (p == null) return;
//         p.setStock((p.getStock() == null ? 0 : p.getStock()) + qty);
//         productRepository.save(p);
//     }
// }
package com.example.productservice.service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequest req) {
        Product p = new Product();
        p.setName(req.getName());
        // p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setStock(req.getStock());
        return productRepository.save(p);
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 扣减库存，用于下单时调用
     */
    public boolean deductStock(Long productId, int quantity) {
        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isPresent()) {
            Product p = opt.get();
            if (p.getStock() >= quantity) {
                p.setStock(p.getStock() - quantity);
                productRepository.save(p);
                return true;
            }
        }
        return false;
    }
}