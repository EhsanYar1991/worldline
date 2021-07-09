package com.worldline.eyar.domain.entity;

import com.worldline.eyar.domain.BaseEntity;
import com.worldline.eyar.domain.FieldMetaData;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PRODUCT")
public class ProductEntity extends BaseEntity {

    @Column(name = "TITLE", unique = true)
    private String title;

    @Max(value = 5, message = "Rate must be less than 5.")
    @Min(value = 1, message = "Rate must be greater than 1.")
    @Column(name = "RATE")
    private Integer rate;

    public interface UserEntityFields extends BaseEntityFields{
        FieldMetaData<String> TITLE = new FieldMetaData<>("title");
        FieldMetaData<String> RATE = new FieldMetaData<>("rate");
    }

}

