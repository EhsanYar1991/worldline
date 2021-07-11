package com.worldline.eyar.service.userrate;

import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.common.request.userrate.UserRateRequest;
import com.worldline.eyar.common.response.userrate.UserRateResponse;
import com.worldline.eyar.domain.BaseEntity;
import com.worldline.eyar.domain.entity.ProductEntity;
import com.worldline.eyar.domain.entity.UserRateEntity;
import com.worldline.eyar.domain.enums.Authority;
import com.worldline.eyar.exception.BusinessException;
import com.worldline.eyar.repository.ProductRepository;
import com.worldline.eyar.repository.UserRateRepository;
import com.worldline.eyar.repository.UserRepository;
import com.worldline.eyar.service.BaseService;
import com.worldline.eyar.service.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserRateService extends BaseService implements ICrudService<UserRateRequest, UserRateResponse, UserRateEntity> {

    private final UserRateRepository userRateRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public UserRateResponse add(UserRateRequest request) throws BusinessException {
        ProductEntity product = productRepository.findById(request.getProductId()).orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Product not found."));
        if (userRateRepository.findByProductAndUser(
                product,
                getCurrentUser())
                .isPresent()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "User has already rated.");
        }
        request.setId(null);
        UserRateEntity save = userRateRepository.saveAndFlush(makeEntity(request));
        updateProductRate(product);
        return makeResponse(save);
    }

    private void updateProductRate(ProductEntity product) {
        List<UserRateEntity> userRates = userRateRepository.findAllByProduct(product);
        product.getUserRates().addAll(userRates);
        product.setRate(
                userRates != null ?
                        userRates.stream().mapToInt(UserRateEntity::getRate).average().orElse(0) :
                        null
        );
        productRepository.save(product);
    }

    @Override
    public UserRateResponse edit(UserRateRequest request) throws BusinessException {
        if (request.getId() == null || request.getId().equals(0L)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "id must be determined");
        }
        UserRateEntity userRate = userRateRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException("User rate not found."));
        if ((!Authority.ADMIN.equals(getCurrentUser().getAuthority())) ||
                (userRate.getUser() != null && !userRate.getUser().getUsername().equals(getCurrentUser().getName()))) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "You are not authorized to change the user rate.");
        }
        ProductEntity product = productRepository.findById(request.getProductId()).orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Product not found."));
        userRate.setProduct(product);
        userRate.setComment(request.getComment());
        userRate.setRate(request.getRate());
        UserRateEntity save = userRateRepository.saveAndFlush(userRate);
        updateProductRate(product);
        return makeResponse(save);
    }

    @Override
    public UserRateResponse delete(Long id) throws BusinessException {
        return activation(id, Boolean.FALSE);
    }

    @Override
    public UserRateResponse activation(Long id, boolean isActive) throws BusinessException {
        UserRateEntity product = getProductById(id);
        product.setActive(isActive);
        return makeResponse(userRateRepository.save(product));
    }

    @Override
    public UserRateResponse get(Long id) throws BusinessException {
        return makeResponse(getProductById(id));
    }

    @Override
    public ListWithTotalSizeResponse<UserRateResponse> list(String search, int pageNumber, int pageSize) throws BusinessException {
        Page<UserRateEntity> page = userRateRepository.findAll((entity, cq, cb) -> {
            final String s = "%" + search.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            if (!isCurrentUserAdmin()) {
                predicates.add(cb.equal(entity.get(BaseEntity.BaseEntityFields.ACTIVE.getField()), Boolean.TRUE));
            }
            predicates.add(cb.like(cb.lower(entity.get(UserRateEntity.UserRateEntityFields.COMMENT.getField())), s));
            return cb.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(pageNumber, pageSize, Sort.by(UserRateEntity.UserRateEntityFields.MODIFICATION_TIME.getField())));
        ListWithTotalSizeResponse<?> listWithTotalSizeResponse = ListWithTotalSizeResponse.builder()
                .list(page.get().map(this::makeResponse).collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalPage(page.getTotalPages())
                .totalSize(page.getTotalElements())
                .build();
        return (ListWithTotalSizeResponse<UserRateResponse>) listWithTotalSizeResponse;
    }

    private UserRateEntity getProductById(Long id) {
        return userRateRepository.findById(id).orElseThrow(() -> new BusinessException("Product not found."));

    }

    @Override
    public UserRateResponse makeResponse(UserRateEntity entity) throws BusinessException {
        return UserRateResponse.builder()
                .id(entity.getId())
                .product(entity.getProduct() != null ?
                        UserRateResponse.LazyProductResponse.builder()
                                .id(entity.getProduct().getId())
                                .title(entity.getProduct().getTitle())
                                .build() :
                        null)
                .user(entity.getUser() != null ?
                        UserRateResponse.LazyUserResponse.builder()
                                .id(entity.getUser().getId())
                                .username(entity.getUser().getUsername())
                                .name(entity.getUser().getName())
                                .lastname(entity.getUser().getLastname())
                                .build() :
                        null)
                .rate(entity.getRate())
                .comment(entity.getComment())
                .lastModifiedBy(entity.getLastModifiedBy())
                .rate(entity.getRate())
                .active(entity.getActive())
                .modificationTime(entity.getModificationTime())
                .submittedTime(entity.getSubmittedTime())
                .build();
    }

    @Override
    public UserRateEntity makeEntity(UserRateRequest request) throws BusinessException {
        UserRateEntity entity = new UserRateEntity();
        entity.setId(request.getId());
        entity.setRate(request.getRate());
        entity.setProduct(productRepository.findById(request.getProductId()).orElseThrow(() -> new BusinessException("Product not found.")));
        entity.setUser(getCurrentUser());
        entity.setComment(request.getComment());
        return entity;
    }
}
