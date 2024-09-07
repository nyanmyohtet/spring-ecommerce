package com.nyanmyohtet.ecommerceservice.service;

import com.nyanmyohtet.ecommerceservice.api.rest.response.SearchProductResponse;
import com.nyanmyohtet.ecommerceservice.model.Product;

public interface ProductService {
    SearchProductResponse searchProduct(String productName, int pageNo, int pageSize, String sortBy, String sortDir);
    Product getProductById(Long studentId);
    Product createProduct(Product product);
}
