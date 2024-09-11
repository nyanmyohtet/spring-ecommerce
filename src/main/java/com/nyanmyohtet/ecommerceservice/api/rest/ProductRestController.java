package com.nyanmyohtet.ecommerceservice.api.rest;

import com.nyanmyohtet.ecommerceservice.api.rest.response.SearchProductResponse;
import com.nyanmyohtet.ecommerceservice.model.Product;
import com.nyanmyohtet.ecommerceservice.service.ProductService;
import com.nyanmyohtet.ecommerceservice.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/product-management/products")
public class ProductRestController {

    private static final Logger logger = LogManager.getLogger(ProductRestController.class);

    private final ProductService productService;

    // Get all products
    @GetMapping
    public ResponseEntity<SearchProductResponse> getAllProduct(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        
        SearchProductResponse allProducts = productService.searchProduct(name, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    // Get a product by ID
    @GetMapping(path = "/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product productNew) {
        Product product = productService.createProduct(productNew);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    // Update an existing product by ID
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable(name = "productId") Long productId,
            @RequestBody Product updatedProduct) {
        Product product = productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok(product);
    }

    // Delete a product by ID
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "productId") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
