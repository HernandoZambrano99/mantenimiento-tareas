package com.empresa.mantenimiento.domain.model.user.gateway;

import com.empresa.mantenimiento.domain.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserOutputPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    void deleteById(Long id);
}
