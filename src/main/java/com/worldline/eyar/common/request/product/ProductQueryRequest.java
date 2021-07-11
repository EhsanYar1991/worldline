package com.worldline.eyar.common.request.product;

import lombok.Data;

import java.util.Set;

@Data
public class ProductQueryRequest {

    private Set<Long> ids;
    private String title;
    private Set<Long> categoryIds;
    private Float minRate;
    private Float maxRate;
    private Float minPrice;
    private Float maxPrice;
    private String description;

    Integer pageNumber = 0;
    Integer pageSize = 15;


}
