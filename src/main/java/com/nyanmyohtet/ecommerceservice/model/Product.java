package com.nyanmyohtet.ecommerceservice.model;

import com.nyanmyohtet.ecommerceservice.enums.ProductCategory;
import com.nyanmyohtet.ecommerceservice.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "products", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@Check(constraints = "category IN ('KID', 'WOMAN', 'MAN') AND status IN ('ENABLED', 'DISABLED')")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "quantity_in_stock", nullable = false)
    private int quantityInStock;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private Boolean taxable;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Boolean visible;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
