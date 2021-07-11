package com.worldline.eyar.domain.entity;

import com.worldline.eyar.domain.BaseEntity;
import com.worldline.eyar.domain.FieldMetaData;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PRODUCT")
public class ProductEntity extends BaseEntity {

    @Column(name = "TITLE", unique = true)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, targetEntity = ProductCategoryEntity.class)
    private ProductCategoryEntity category;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private Float price;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String.class)
    @Size(max = 5, message = "number of images must be between 0 to 5")
    private Set<String> imagesUrl;

    @Column(name = "RATE")
    private Double rate;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, targetEntity = UserRateEntity.class)
    private Set<UserRateEntity> userRates = new HashSet<>();


    public interface ProductEntityFields extends BaseEntityFields{
        FieldMetaData<String> TITLE = new FieldMetaData<>("title");
        FieldMetaData<Float> RATE = new FieldMetaData<>("rate");
        FieldMetaData<Float> PRICE = new FieldMetaData<>("price");
        FieldMetaData<String> DESCRIPTION = new FieldMetaData<>("description");
        FieldMetaData<Set<UserRateEntity>> USER_RATES = new FieldMetaData<>("userRates");
        FieldMetaData<ProductCategoryEntity> CATEGORY = new FieldMetaData<>("category");
    }

}

