package com.worldline.eyar.domain.entity;

import com.worldline.eyar.domain.BaseEntity;
import com.worldline.eyar.domain.FieldMetaData;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PRODUCT_CATEGORY")
public class ProductCategoryEntity extends BaseEntity {

    @Column(name = "TITLE", unique = true)
    private String title;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, targetEntity = ProductCategoryEntity.class)
    private ProductCategoryEntity parent;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, targetEntity = ProductEntity.class)
    private Set<ProductEntity> products;

    public interface ProductCategoryEntityFields extends BaseEntityFields{
        FieldMetaData<String> TITLE = new FieldMetaData<>("title");
        FieldMetaData<ProductCategoryEntity> PARENT = new FieldMetaData<>("parent");
        FieldMetaData<Set<ProductEntity>> PRODUCTS = new FieldMetaData<>("products");
    }

}

