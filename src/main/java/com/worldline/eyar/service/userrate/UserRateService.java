package com.worldline.eyar.service.userrate;

import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.common.request.userrate.UserRateRequest;
import com.worldline.eyar.common.response.userrate.UserRateResponse;
import com.worldline.eyar.domain.entity.ProductEntity;
import com.worldline.eyar.domain.entity.UserEntity;
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
        if (userRateRepository.findByProductAndUser(
                productRepository.findById(request.getProductId()).orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Product not found.")),
                getCurrentUser())
                .isPresent()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "User has already rated.");
        }
        request.setId(null);
        return makeResponse(userRateRepository.save(makeEntity(request)));
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

        userRate.setProduct(productRepository.findById(request.getProductId()).orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Product not found.")));
        userRate.setComment(request.getComment());
        userRate.setRate(request.getRate());
        return makeResponse(userRateRepository.save(userRate));
    }

    @Override
    public UserRateResponse delete(Long id) throws BusinessException {
        UserRateEntity product = getProductById(id);
        userRateRepository.delete(product);
        return makeResponse(product);
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
            List<UserEntity> users = userRepository.findAll((userRoot, ucq, ucb) -> ucb.and(
                    ucb.like(userRoot.get(UserEntity.UserEntityFields.USERNAME.getField()), s),
                    ucb.like(userRoot.get(UserEntity.UserEntityFields.NAME.getField()), s),
                    ucb.like(userRoot.get(UserEntity.UserEntityFields.LAST_NAME.getField()), s),
                    ucb.like(userRoot.get(UserEntity.UserEntityFields.EMAIL.getField()), s),
                    ucb.equal(userRoot.get(ProductEntity.ProductEntityFields.ACTIVE.getField()), Boolean.TRUE)
            ));
            List<ProductEntity> products = productRepository.findAll((productRoot, pcq, pcb) -> cb.and(
                    pcb.like(productRoot.get(ProductEntity.ProductEntityFields.TITLE.getField()), s),
                    pcb.equal(productRoot.get(ProductEntity.ProductEntityFields.ACTIVE.getField()), Boolean.TRUE)
            ));
            return cb.and(
                    cb.or(
                            cb.in(entity.get(UserRateEntity.UserRateEntityFields.PRODUCT.getField()).in(products)),
                            cb.in(entity.get(UserRateEntity.UserRateEntityFields.USER.getField()).in(users))),
                    cb.equal(entity.get(UserRateEntity.UserRateEntityFields.ACTIVE.getField()), Boolean.TRUE)
            );
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
        entity.setUser(userRepository.findByUsername(getCurrentUser().getName()).orElseThrow(() -> new BusinessException("User not found.")));
        entity.setComment(request.getComment());
        return entity;
    }
}
