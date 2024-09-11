package com.nyanmyohtet.ecommerceservice.service.Impl;

import com.nyanmyohtet.ecommerceservice.api.rest.response.SearchProductResponse;
import com.nyanmyohtet.ecommerceservice.exception.ResourceNotFoundException;
import com.nyanmyohtet.ecommerceservice.model.Product;
import com.nyanmyohtet.ecommerceservice.repository.ProductRepository;
import com.nyanmyohtet.ecommerceservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Override
    public SearchProductResponse searchProduct(String productName, int pageNo, int pageSize, String sortBy, String sortDir) {
        logger.debug("Searching products with params - productName: {}, pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                productName, pageNo, pageSize, sortBy, sortDir);

        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.ASC), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<Product> spec = Specification.where(null);

        if (StringUtils.hasText(productName)) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("name"), productName));
        }

        Page<Product> productsPage = productRepository.findAll(spec, pageable);
        List<Product> products = productsPage.getContent();

        logger.debug("Search results - totalElements: {}, totalPages: {}, currentPage: {}, pageSize: {}, isLast: {}",
                productsPage.getTotalElements(), productsPage.getTotalPages(), productsPage.getNumber(), productsPage.getSize(), productsPage.isLast());

        return new SearchProductResponse(
                products,
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages(),
                productsPage.isLast()
        );
    }

    // Retrieve a product by ID
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    // Create a new product
    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setSku(updatedProduct.getSku());
        existingProduct.setWeight(updatedProduct.getWeight());
        existingProduct.setTaxable(updatedProduct.getTaxable());
        existingProduct.setStatus(updatedProduct.getStatus());
        existingProduct.setVisible(updatedProduct.getVisible());

        return productRepository.save(existingProduct);
    }

    // Delete a product by ID
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        productRepository.delete(product);
    }
}
