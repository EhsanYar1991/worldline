package com.worldline.eyar.repository;

import com.worldline.eyar.domain.entity.ProductEntity;
import com.worldline.eyar.domain.entity.UserEntity;
import com.worldline.eyar.domain.entity.UserRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRateRepository extends JpaRepository<UserRateEntity, Long>, JpaSpecificationExecutor<UserRateEntity> {
    Optional<Object> findByProductAndUser(ProductEntity product, UserEntity user);
}
