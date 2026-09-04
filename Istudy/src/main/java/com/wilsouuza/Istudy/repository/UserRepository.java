package com.wilsouuza.Istudy.repository;

import com.wilsouuza.Istudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.UnknownServiceException;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
