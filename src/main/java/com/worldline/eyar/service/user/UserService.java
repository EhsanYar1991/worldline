package com.worldline.eyar.service.user;

import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.common.request.user.UserRequest;
import com.worldline.eyar.common.response.UserResponse;
import com.worldline.eyar.domain.entity.UserEntity;
import com.worldline.eyar.exception.BusinessException;
import com.worldline.eyar.repository.UserRepository;
import com.worldline.eyar.service.BaseService;
import com.worldline.eyar.service.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService
        extends BaseService
        implements UserDetailsService,
        ICrudService<UserRequest, UserResponse, UserEntity> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserResponse changePassword(String username, String password) {
        UserEntity user = getUserByUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return makeResponse(userRepository.save(user));
    }

    public UserEntity getUserByUsername(String username) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new BusinessException(String.format("%s does not exist.", username));
        }
        return userOpt.get();
    }

    private UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User does not exist."));
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("%s does not exist.",username)));
    }

    @Override
    public UserResponse add(UserRequest userRequest) throws BusinessException {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()){
            throw new BusinessException(String.format("%s is duplicated",userRequest.getUsername()));
        }
        return makeResponse(userRepository.save(makeEntity(userRequest)));
    }

    @Override
    public UserResponse edit(UserRequest edit_request) throws BusinessException {
        return null;
    }

    @Override
    public UserResponse delete(Long id) throws BusinessException {
        return null;
    }

    @Override
    public UserResponse activation(Long id, boolean isActive) throws BusinessException {
        UserEntity user = getUserById(id);
        user.setActive(Boolean.FALSE);
        return makeResponse(userRepository.save(user));
    }

    @Override
    public UserResponse get(Long id) throws BusinessException {
        return null;
    }

    @Override
    public ListWithTotalSizeResponse<UserResponse> list(String search, int pageNumber, int pageSize) throws BusinessException {
        return null;
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
                .version(entity.getVersion())
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
