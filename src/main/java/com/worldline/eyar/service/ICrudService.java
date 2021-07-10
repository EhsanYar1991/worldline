package com.worldline.eyar.service;


import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.domain.DBEntity;
import com.worldline.eyar.exception.BusinessException;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public interface ICrudService<REQUEST, RESPONSE extends Serializable, ENTITY extends DBEntity> {
    RESPONSE add(@NotNull REQUEST request) throws BusinessException;
    RESPONSE edit(@NotNull REQUEST edit_request) throws BusinessException;
    RESPONSE delete(Long id) throws BusinessException;
    RESPONSE activation(@NotNull Long id, boolean isActive) throws BusinessException;
    RESPONSE get(@NotNull Long id) throws BusinessException;
    ListWithTotalSizeResponse<RESPONSE> list(String search, int pageNumber , int pageSize) throws BusinessException;
    RESPONSE makeResponse(@NotNull ENTITY entity) throws BusinessException;
    ENTITY makeEntity(@NotNull REQUEST request) throws BusinessException;
}
