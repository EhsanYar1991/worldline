package com.worldline.eyar.common.request.userrate;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UserRateRequest {

    private Long id;

    @NotNull(message = "product id must be determined")
    private Long productId;

    private String comment;

    @Max(value = 5, message = "Rate must be less or equal than 5.")
    @Min(value = 1, message = "Rate must be greater or equal than 1.")
    private Integer rate = 1;

}
