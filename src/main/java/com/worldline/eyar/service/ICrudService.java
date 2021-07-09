package com.worldline.eyar.service;


import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.domain.DBEntity;
import com.worldline.eyar.exception.BusinessException;

import java.io.Serializable;

public interface ICrudService<REQUEST, RESPONSE extends Serializable, ENTITY extends DBEntity> {
    RESPONSE add(REQUEST request) throws BusinessException;
    RESPONSE edit(REQUEST edit_request) throws BusinessException;
    RESPONSE delete(Long id) throws BusinessException;
    RESPONSE activation(Long id, boolean isActive) throws BusinessException;
    RESPONSE get(Long id) throws BusinessException;
    ListWithTotalSizeResponse<RESPONSE> list(String search, int pageNumber , int pageSize) throws BusinessException;
    RESPONSE makeResponse(ENTITY entity) throws BusinessException;
}
