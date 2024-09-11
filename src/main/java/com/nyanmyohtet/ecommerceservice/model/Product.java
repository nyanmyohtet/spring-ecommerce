package com.nyanmyohtet.ecommerceservice.model;

import com.nyanmyohtet.ecommerceservice.enums.ProductCategory;
import com.nyanmyohtet.ecommerceservice.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

import javax.persistence.*;
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

    private String name;
    private String description;
    private Double price;
    private Integer stock;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String sku;

    private Double weight;


    private Boolean taxable;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Boolean visible;

    @Column()
    private Date createdDate;

    @Column()
    private Date updatedDate;

    @PrePersist
    private void prePersist() {
        this.createdDate = Calendar.getInstance().getTime();
        this.updatedDate = Calendar.getInstance().getTime();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedDate = Calendar.getInstance().getTime();
    }

}
