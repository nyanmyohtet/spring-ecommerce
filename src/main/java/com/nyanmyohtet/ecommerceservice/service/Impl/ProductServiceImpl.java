package com.nyanmyohtet.ecommerceservice.service.Impl;

import com.nyanmyohtet.ecommerceservice.api.rest.response.SearchProductResponse;
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
import java.util.Optional;

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

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            // throw new ResourceNotFoundException("product not found");
        }

        return productOptional.get();
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
