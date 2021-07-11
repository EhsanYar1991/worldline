package com.worldline.eyar.common.response.productcategory;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
public class ProductCategoryResponse implements Serializable {

    private Long id;
    private String title;
    private Long parentId;
    private String generatedBy;
    private String lastModifiedBy;
    private Instant submittedTime;
    private Instant modificationTime;

}
