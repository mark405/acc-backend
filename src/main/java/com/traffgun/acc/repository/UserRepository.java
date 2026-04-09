package com.traffgun.acc.repository;

import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsernameAndActiveIsTrue(String username);

    Optional<User> findByIdAndActiveIsTrue(Long id);

    boolean existsByUsernameAndActiveIsTrue(String username);

    List<User> findAllByRole(UserRole role);
}
