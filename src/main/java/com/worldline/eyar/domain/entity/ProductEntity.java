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

    @Column(name = "DESCRIPTION", unique = true)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String.class)
    @Size(min = 0, max = 5, message = "number of images must be between 0 to 5")
    private Set<String> imagesUrl;

    @Transient
    private Double rate;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, targetEntity = UserRateEntity.class)
    private Set<UserRateEntity> userRateEntities = new HashSet<>();


    public Double getRate() {
        return userRateEntities != null ?
                userRateEntities.stream().mapToInt(UserRateEntity::getRate).average().orElse(0) :
                null;
    }

    public interface ProductEntityFields extends BaseEntityFields{
        FieldMetaData<String> TITLE = new FieldMetaData<>("title");
        FieldMetaData<String> RATE = new FieldMetaData<>("rate");
        FieldMetaData<String> DESCRIPTION = new FieldMetaData<>("description");
    }

}

