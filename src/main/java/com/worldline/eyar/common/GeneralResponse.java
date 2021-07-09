package com.worldline.eyar.common;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GeneralResponse<T extends Serializable> implements Serializable {
    private HttpStatus status;
    private T body;
}
