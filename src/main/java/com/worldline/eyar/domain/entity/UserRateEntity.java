package com.worldline.eyar.domain.entity;

import com.worldline.eyar.domain.BaseEntity;
import com.worldline.eyar.domain.FieldMetaData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "USER_RATE")
public class UserRateEntity extends BaseEntity {


    @NotNull(message = "product must be determined")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, optional = false, targetEntity = ProductEntity.class)
    private ProductEntity product;

    @NotNull(message = "user must be determined")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, optional = false, targetEntity = UserEntity.class)
    private UserEntity user;

    @Max(value = 5, message = "Rate must be less than 5.")
    @Min(value = 1, message = "Rate must be greater than 1.")
    @Column(name = "RATE")
    private Integer rate;

    @Column(name = "COMMENT")
    private String comment;


    public interface UserRateEntityFields extends BaseEntityFields{
        FieldMetaData<Long> PRODUCT_ID = new FieldMetaData<>("productId");
        FieldMetaData<Long> USER_ID = new FieldMetaData<>("userId");
        FieldMetaData<Integer> RATE = new FieldMetaData<>("rate");
        FieldMetaData<String> COMMENT = new FieldMetaData<>("comment");
    }

}
