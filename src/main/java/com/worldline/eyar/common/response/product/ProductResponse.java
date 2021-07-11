package com.worldline.eyar.common.response.product;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class ProductResponse implements Serializable {

    private Long id;
    private LazyProductCategoryResponse category;
    private String title;
    private String description;
    private Set<String> imagesUrl;
    private Double rate;
    private Float price;
    private String generatedBy;
    private String lastModifiedBy;
    private Instant submittedTime;
    private Instant modificationTime;

    @Data
    @Builder
    public static class LazyProductCategoryResponse{
        private Long id;
        private String title;
    }


}
