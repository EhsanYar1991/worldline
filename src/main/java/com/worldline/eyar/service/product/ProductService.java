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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProductService extends BaseService implements ICrudService<ProductRequest, ProductResponse, ProductEntity> {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse add(ProductRequest request) throws BusinessException {
        if (productRepository.findByTitle(request.getTitle()).isPresent()) {
            throw new BusinessException(String.format("%s is duplicated", request.getTitle()));
        }
        request.setId(null);
        return makeResponse(productRepository.save(makeEntity(request)));
    }

    @Override
    public ProductResponse edit(ProductRequest request) throws BusinessException {
        if (request.getId() == null || request.getId().equals(0L)) {
            throw new BusinessException("id must be determined");
        }
        ProductEntity product = productRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException("Product not found."));
        productRepository.findByTitle(request.getTitle()).ifPresent(byTitle -> {
            if (!byTitle.getId().equals(request.getId())) {
                throw new BusinessException("title is duplicated.");
            }
        });
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setImagesUrl(request.getImagesUrl());
        return makeResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse delete(Long id) throws BusinessException {
        ProductEntity product = getProductById(id);
        productRepository.delete(product);
        return makeResponse(product);
    }

    @Override
    public ProductResponse activation(Long id, boolean isActive) throws BusinessException {
        ProductEntity product = getProductById(id);
        product.setActive(isActive);
        return makeResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse get(Long id) throws BusinessException {
        return makeResponse(getProductById(id));
    }

    @Override
    public ListWithTotalSizeResponse<ProductResponse> list(String search, int pageNumber, int pageSize) throws BusinessException {
        Page<ProductEntity> page = productRepository.findAll((entity, cq, cb) -> {
            final String s = "%" + search.toLowerCase() + "%";
            return cb.and(
                    cb.like(entity.get(ProductEntity.ProductEntityFields.TITLE.getField()), s),
                    cb.like(entity.get(ProductEntity.ProductEntityFields.DESCRIPTION.getField()), s),
                    cb.equal(entity.get(ProductEntity.ProductEntityFields.ACTIVE.getField()), Boolean.TRUE)
            );
        }, PageRequest.of(pageNumber, pageSize, Sort.by(ProductEntity.ProductEntityFields.MODIFICATION_TIME.getField())));
        ListWithTotalSizeResponse<?> listWithTotalSizeResponse = ListWithTotalSizeResponse.builder()
                .list(page.get().map(this::makeResponse).collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalPage(page.getTotalPages())
                .totalSize(page.getTotalElements())
                .build();
        return (ListWithTotalSizeResponse<ProductResponse>) listWithTotalSizeResponse;
    }

    private ProductEntity getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new BusinessException("Product not found."));

    }

    @Override
    public ProductResponse makeResponse(ProductEntity entity) throws BusinessException {
        return ProductResponse.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .imagesUrl(entity.getImagesUrl())
                .lastModifiedBy(entity.getLastModifiedBy())
                .rate(entity.getRate())
                .modificationTime(entity.getModificationTime())
                .submittedTime(entity.getSubmittedTime())
                .title(entity.getTitle())
                .title(entity.getTitle())
                .build();
    }

    @Override
    public ProductEntity makeEntity(ProductRequest request) throws BusinessException {
        ProductEntity product = new ProductEntity();
        product.setId(request.getId());
        product.setImagesUrl(request.getImagesUrl());
        product.setDescription(request.getDescription());
        product.setTitle(request.getTitle());
        return product;
    }
}
