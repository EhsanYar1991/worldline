package com.worldline.eyar.service.product;

import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.common.request.product.ProductRequest;
import com.worldline.eyar.common.response.product.ProductResponse;
import com.worldline.eyar.domain.entity.ProductEntity;
import com.worldline.eyar.exception.BusinessException;
import com.worldline.eyar.repository.ProductRepository;
import com.worldline.eyar.service.BaseService;
import com.worldline.eyar.service.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ProductService extends BaseService implements ICrudService<ProductRequest, ProductResponse, ProductEntity> {

    private final ProductRepository productRepository;


    @Override
    public ProductResponse add(ProductRequest productRequest) throws BusinessException {
        return null;
    }

    @Override
    public ProductResponse edit(ProductRequest edit_request) throws BusinessException {
        return null;
    }

    @Override
    public ProductResponse delete(Long id) throws BusinessException {
        return null;
    }

    @Override
    public ProductResponse activation(Long id, boolean isActive) throws BusinessException {
        return null;
    }

    @Override
    public ProductResponse get(Long id) throws BusinessException {
        return null;
    }

    @Override
    public ListWithTotalSizeResponse<ProductResponse> list(String search, int pageNumber, int pageSize) throws BusinessException {
        return null;
    }

    @Override
    public ProductResponse makeResponse(ProductEntity entity) throws BusinessException {
        return null;
    }

    @Override
    public ProductEntity makeEntity(ProductRequest productRequest) throws BusinessException {
        return null;
    }
}
