package com.worldline.eyar.service.user;

import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.common.request.user.UserRequest;
import com.worldline.eyar.common.response.user.UserResponse;
import com.worldline.eyar.domain.BaseEntity;
import com.worldline.eyar.domain.entity.UserEntity;
import com.worldline.eyar.exception.BusinessException;
import com.worldline.eyar.repository.UserRepository;
import com.worldline.eyar.service.BaseService;
import com.worldline.eyar.service.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService
        extends BaseService
        implements UserDetailsService,
        ICrudService<UserRequest, UserResponse, UserEntity> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserResponse changePassword(Long id, String oldPassword, String newPassword) {
        UserEntity user = getUserById(id);
        if (!passwordEncoder.encode(oldPassword).equals(user.getPassword())) {
            throw new BusinessException("old password is incorrect.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return makeResponse(userRepository.save(user));
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new BusinessException(String.format("%s does not exist.", username)));
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException("User does not exist."));
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("%s does not exist.", username)));
    }

    @Override
    public UserResponse add(UserRequest request) throws BusinessException {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException(String.format("%s is duplicated", request.getUsername()));
        }
        return makeResponse(userRepository.save(makeEntity(request)));
    }

    @Override
    public UserResponse edit(UserRequest request) throws BusinessException {
        if (request.getId() == null || request.getId().equals(0L)) {
            throw new BusinessException("id must be determined");
        }
        UserEntity user = userRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException("User not found."));
        if (!(isCurrentUserAdmin() || getCurrentUser().getId().equals(request.getId())) ||
                (!user.getActive() && !isCurrentUserAdmin())){
            throw new BusinessException(HttpStatus.FORBIDDEN, "You are not authorized to edit user.");
        }
        userRepository.findByUsername(request.getUsername()).ifPresent(byUsername -> {
            if (!byUsername.getId().equals(request.getId())) {
                throw new BusinessException("username is duplicated.");
            }
        });
        return makeResponse(makeEntity(request));
    }

    @Override
    public UserResponse delete(Long id) throws BusinessException {
        UserEntity user = getUserById(id);
        userRepository.delete(user);
        return makeResponse(user);
    }

    @Override
    public UserResponse activation(Long id, boolean isActive) throws BusinessException {
        UserEntity user = getUserById(id);
        user.setActive(Boolean.FALSE);
        return makeResponse(userRepository.save(user));
    }

    @Override
    public UserResponse get(Long id) throws BusinessException {
        return makeResponse(getUserById(id));
    }

    @Override
    public ListWithTotalSizeResponse<UserResponse> list(String search, int pageNumber, int pageSize) throws BusinessException {
        Page<UserEntity> page = userRepository.findAll((entity, cq, cb) -> {
            final String s = "%" + search.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            if (!isCurrentUserAdmin()){
                predicates.add(cb.equal(entity.get(BaseEntity.BaseEntityFields.ACTIVE.getField()), Boolean.TRUE));
            }
            predicates.add(
                    cb.or(
                            cb.like(cb.lower(entity.get(UserEntity.UserEntityFields.USERNAME.getField())), s),
                            cb.like(cb.lower(entity.get(UserEntity.UserEntityFields.NAME.getField())), s),
                            cb.like(cb.lower(entity.get(UserEntity.UserEntityFields.LAST_NAME.getField())), s),
                            cb.like(cb.lower(entity.get(UserEntity.UserEntityFields.EMAIL.getField())), s)
                    )
            );
            return cb.and(predicates.toArray(new Predicate[0]));

        }, PageRequest.of(pageNumber, pageSize, Sort.by(UserEntity.UserEntityFields.MODIFICATION_TIME.getField())));
        ListWithTotalSizeResponse<?> listWithTotalSizeResponse = ListWithTotalSizeResponse.builder()
                .list(page.get().map(this::makeResponse).collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalPage(page.getTotalPages())
                .totalSize(page.getTotalElements())
                .build();
        return (ListWithTotalSizeResponse<UserResponse>) listWithTotalSizeResponse;
    }

    @Override
    public UserResponse makeResponse(UserEntity entity) throws BusinessException {
        return UserResponse.builder()
                .id(entity.getId())
                .active(entity.getActive())
                .authority(entity.getAuthority())
                .email(entity.getEmail())
                .generatedBy(entity.getGeneratedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .lastname(entity.getLastname())
                .modificationTime(entity.getModificationTime())
                .name(entity.getName())
                .username(entity.getUsername())
                .submittedTime(entity.getSubmittedTime())
                .build();
    }

    @Override
    public UserEntity makeEntity(UserRequest request) throws BusinessException {
        UserEntity entity = new UserEntity();
        entity.setId(request.getId());
        entity.setUsername(request.getUsername());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setEmail(request.getEmail());
        entity.setAuthority(request.getAuthority());
        entity.setEmail(request.getEmail());
        entity.setName(request.getName());
        entity.setLastname(request.getLastname());
        return entity;
    }

}
