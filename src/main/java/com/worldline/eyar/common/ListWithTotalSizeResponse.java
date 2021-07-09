package com.worldline.eyar.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListWithTotalSizeResponse<RESPONSE extends Serializable> implements Serializable {
    private long totalSize;
    private long totalPage;
    private int page;
    private int size;
    private List<RESPONSE> list;
}
