package com.worldline.eyar.common.response.userrate;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
public class UserRateResponse implements Serializable {
    private Long id;
    private LazyProductResponse product;
    private LazyUserResponse user;
    private Integer rate;
    private String comment;
    private String lastModifiedBy;
    private Instant submittedTime;
    private Instant modificationTime;
    private Integer version;

    @Data
    @Builder
    public class LazyUserResponse {
        private Long id;
        private String username;
        private String name;
        private String lastname;
    }

    @Data
    @Builder
    public class LazyProductResponse {
        private Long id;
        private String title;
    }

}
