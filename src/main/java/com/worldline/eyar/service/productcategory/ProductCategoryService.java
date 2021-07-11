package com.worldline.eyar.service.productcategory;

import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.common.request.productcategory.ProductCategoryRequest;
import com.worldline.eyar.common.response.productcategory.ProductCategoryResponse;
import com.worldline.eyar.domain.entity.ProductCategoryEntity;
import com.worldline.eyar.exception.BusinessException;
import com.worldline.eyar.repository.ProductCategoryRepository;
import com.worldline.eyar.service.BaseService;
import com.worldline.eyar.service.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProductCategoryService extends BaseService implements ICrudService<ProductCategoryRequest, ProductCategoryResponse, ProductCategoryEntity> {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategoryResponse add(ProductCategoryRequest request) throws BusinessException {
        if (productCategoryRepository.findByTitle(request.getTitle()).isPresent()) {
            throw new BusinessException(String.format("%s is duplicated", request.getTitle()));
        }
        request.setId(null);
        return makeResponse(productCategoryRepository.save(makeEntity(request)));
    }

    @Override
    public ProductCategoryResponse edit(ProductCategoryRequest request) throws BusinessException {
        if (request.getId() == null || request.getId().equals(0L)) {
            throw new BusinessException("id must be determined");
        }
        ProductCategoryEntity category = productCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException("Product category not found."));
        if (!category.getActive() && !isCurrentUserAdmin()){
            throw new BusinessException(HttpStatus.FORBIDDEN, "You are not authorized to edit category.");
        }
        productCategoryRepository.findByTitle(request.getTitle()).ifPresent(byTitle -> {
            if (!byTitle.getId().equals(request.getId())) {
                throw new BusinessException("title is duplicated.");
            }
        });
        category.setTitle(request.getTitle());
        category.setParent(getProductCategoryById(request.getParentId()));
        return makeResponse(productCategoryRepository.save(category));
    }

    @Override
    public ProductCategoryResponse delete(Long id) throws BusinessException {
        ProductCategoryEntity category = getProductCategoryById(id);
        productCategoryRepository.delete(category);
        return makeResponse(category);
    }

    @Override
    public ProductCategoryResponse activation(Long id, boolean isActive) throws BusinessException {
        ProductCategoryEntity category = getProductCategoryById(id);
        category.setActive(isActive);
        return makeResponse(productCategoryRepository.save(category));
    }

    @Override
    public ProductCategoryResponse get(Long id) throws BusinessException {
        return makeResponse(getProductCategoryById(id));
    }

    @Override
    public ListWithTotalSizeResponse<ProductCategoryResponse> list(String search, int pageNumber, int pageSize) throws BusinessException {
        Page<ProductCategoryEntity> page = productCategoryRepository.findAll((entity, cq, cb) -> {
            final String s = "%" + search.toLowerCase() + "%";
            if (!isCurrentUserAdmin()){
                cb.and(cb.equal(entity.get(ProductCategoryEntity.ProductCategoryEntityFields.ACTIVE.getField()), Boolean.TRUE));
            }
            return cb.and(
                    cb.like(cb.lower(entity.get(ProductCategoryEntity.ProductCategoryEntityFields.TITLE.getField())), s)
            );
        }, PageRequest.of(pageNumber, pageSize, Sort.by(ProductCategoryEntity.ProductCategoryEntityFields.MODIFICATION_TIME.getField())));
        ListWithTotalSizeResponse<?> listWithTotalSizeResponse = ListWithTotalSizeResponse.builder()
                .list(page.get().map(this::makeResponse).collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalPage(page.getTotalPages())
                .totalSize(page.getTotalElements())
                .build();
        return (ListWithTotalSizeResponse<ProductCategoryResponse>) listWithTotalSizeResponse;
    }

    private ProductCategoryEntity getProductCategoryById(Long id) {
        return productCategoryRepository.findById(id).orElseThrow(() -> new BusinessException("Product category not found."));
    }

    @Override
    public ProductCategoryResponse makeResponse(ProductCategoryEntity entity) throws BusinessException {
        return ProductCategoryResponse.builder()
                .id(entity.getId())
                .modificationTime(entity.getModificationTime())
                .submittedTime(entity.getSubmittedTime())
                .generatedBy(entity.getGeneratedBy())
                .title(entity.getTitle())
                .parentId(entity.getParent() != null ? entity.getParent().getId() : null)
                .build();
    }

    @Override
    public ProductCategoryEntity makeEntity(ProductCategoryRequest request) throws BusinessException {
        ProductCategoryEntity product = new ProductCategoryEntity();
        product.setId(request.getId());
        product.setParent(getProductCategoryById(request.getParentId()));
        product.setTitle(request.getTitle());
        return product;
    }
}
