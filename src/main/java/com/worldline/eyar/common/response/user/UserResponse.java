package com.worldline.eyar.common.response.user;


import com.worldline.eyar.domain.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {

    private Long id;
    private String username;
    private Authority authority;
    private String email;
    private String name;
    private String lastname;
    private Boolean active;
    private String generatedBy;
    private String lastModifiedBy;
    private Instant submittedTime;
    private Instant modificationTime;

}
