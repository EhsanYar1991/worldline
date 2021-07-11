package com.worldline.eyar.service.product;

import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.common.request.product.ProductQueryRequest;
import com.worldline.eyar.common.request.product.ProductRequest;
import com.worldline.eyar.common.response.product.ProductResponse;
import com.worldline.eyar.domain.BaseEntity;
import com.worldline.eyar.domain.entity.ProductEntity;
import com.worldline.eyar.exception.BusinessException;
import com.worldline.eyar.repository.ProductCategoryRepository;
import com.worldline.eyar.repository.ProductRepository;
import com.worldline.eyar.service.BaseService;
import com.worldline.eyar.service.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProductService extends BaseService implements ICrudService<ProductRequest, ProductResponse, ProductEntity> {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

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
        if (!product.getActive() && !isCurrentUserAdmin()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "You are not authorized to edit product.");
        }
        productRepository.findByTitle(request.getTitle()).ifPresent(byTitle -> {
            if (!byTitle.getId().equals(request.getId())) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "title is duplicated.");
            }
        });
        product.setCategory(productCategoryRepository.findById(request.getProductCategoryId()).orElseThrow(
                () -> new BusinessException(HttpStatus.BAD_REQUEST, "Product category not found.")
        ));
        product.setPrice(request.getPrice());
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
        return activation(id,Boolean.FALSE);
    }

    @Override
    public ProductResponse get(Long id) throws BusinessException {
        return makeResponse(getProductById(id));
    }

    @Override
    public ListWithTotalSizeResponse<ProductResponse> list(String search, int pageNumber, int pageSize) throws BusinessException {
        Page<ProductEntity> page = productRepository.findAll((entity, cq, cb) -> {
            final String s = "%" + search.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            if (!isCurrentUserAdmin()) {
                predicates.add(cb.equal(entity.get(BaseEntity.BaseEntityFields.ACTIVE.getField()), Boolean.TRUE));
            }
            predicates.add(cb.and(
                    cb.like(entity.get(ProductEntity.ProductEntityFields.TITLE.getField()), s),
                    cb.like(entity.get(ProductEntity.ProductEntityFields.DESCRIPTION.getField()), s)
            ));
            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        }, PageRequest.of(pageNumber, pageSize, Sort.by(ProductEntity.ProductEntityFields.MODIFICATION_TIME.getField())));
        return generateListResponse(page);
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
                .price(entity.getPrice())
                .category(entity.getCategory() != null ?
                        ProductResponse.LazyProductCategoryResponse.builder().id(entity.getCategory().getId()).title(entity.getCategory().getTitle()).build() :
                        null
                )
                .modificationTime(entity.getModificationTime())
                .submittedTime(entity.getSubmittedTime())
                .title(entity.getTitle())
                .title(entity.getTitle())
                .build();
    }

    @Override
    public ProductEntity makeEntity(ProductRequest request) throws BusinessException {
        ProductEntity product = new ProductEntity();
        product.setCategory(productCategoryRepository.findById(request.getProductCategoryId()).orElseThrow(
                () -> new BusinessException(HttpStatus.BAD_REQUEST, "Product category not found.")
        ));
        product.setPrice(request.getPrice());
        product.setId(request.getId());
        product.setImagesUrl(request.getImagesUrl());
        product.setDescription(request.getDescription());
        product.setTitle(request.getTitle());
        return product;
    }

    public ListWithTotalSizeResponse<ProductResponse> query(ProductQueryRequest request) {
        Page<ProductEntity> page = productRepository.findAll(generateQuerySpecification(request)
                , PageRequest.of(
                        request.getPageNumber(),
                        request.getPageSize(),
                        Sort.by(ProductEntity.ProductEntityFields.MODIFICATION_TIME.getField()))
        );
        return generateListResponse(page);
    }

    private Specification<ProductEntity> generateQuerySpecification(ProductQueryRequest request){
        return (entity, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!isCurrentUserAdmin()) {
                predicates.add(cb.equal(entity.get(BaseEntity.BaseEntityFields.ACTIVE.getField()), Boolean.TRUE));
            }
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(entity.get(ProductEntity.ProductEntityFields.TITLE.getField())),
                                likeQueryValue(request.getTitle()))
                );
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(entity.get(ProductEntity.ProductEntityFields.DESCRIPTION.getField())),
                                likeQueryValue(request.getDescription()))
                );
            }
            if (request.getMinPrice() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                entity.get(ProductEntity.ProductEntityFields.PRICE.getField()),
                                request.getMinPrice())
                );
            }
            if (request.getMaxPrice() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(entity.get(ProductEntity.ProductEntityFields.PRICE.getField()),
                                request.getMaxPrice())
                );
            }
            if (request.getMinRate() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                entity.get(ProductEntity.ProductEntityFields.RATE.getField()),
                                request.getMinRate())
                );
            }
            if (request.getMaxRate() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                entity.get(ProductEntity.ProductEntityFields.RATE.getField()),
                                request.getMaxRate())
                );
            }
            if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
                predicates.add(cb.in(entity
                                .get(ProductEntity.ProductEntityFields.CATEGORY.getField())
                                .in(productCategoryRepository.findAllById(request.getCategoryIds()))
                        )
                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private String likeQueryValue(String value) {
        return "%" + value.toLowerCase() + "%";
    }

    private ListWithTotalSizeResponse<ProductResponse> generateListResponse(Page<ProductEntity> page) {
        ListWithTotalSizeResponse<?> listWithTotalSizeResponse = ListWithTotalSizeResponse.builder()
                .list(page.get().map(this::makeResponse).collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalPage(page.getTotalPages())
                .totalSize(page.getTotalElements())
                .build();
        return (ListWithTotalSizeResponse<ProductResponse>) listWithTotalSizeResponse;
    }


}
