package com.forge.sslsj.repository;

import com.forge.sslsj.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefaultUserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}