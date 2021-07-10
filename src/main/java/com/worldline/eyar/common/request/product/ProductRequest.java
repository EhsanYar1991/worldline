package com.worldline.eyar.common.request.product;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class ProductRequest {

    private Long id;

    @NotBlank(message = "title must be determined.")
    private String title;

    @NotBlank(message = "description must be determined.")
    private String description;

    private Set<String> imagesUrl;


}
