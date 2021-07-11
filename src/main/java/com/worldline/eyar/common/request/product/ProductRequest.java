package com.worldline.eyar.common.request.product;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class ProductRequest {

    private Long id;

    @NotNull(message = "product category id must be determined.")
    private Long productCategoryId;

    @NotNull(message = "price must be determined.")
    private Float price;

    @NotBlank(message = "title must be determined.")
    private String title;

    @NotBlank(message = "description must be determined.")
    private String description;

    private Set<String> imagesUrl;


}
