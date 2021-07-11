package com.worldline.eyar.common.request.productcategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryRequest {

    private Long id;

    @NotBlank(message = "title must be determined.")
    private String title;

    private Long parentId;

}
