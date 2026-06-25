package com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.adapter;

import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.domain.model.user.gateway.UserOutputPort;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.mapper.UserEntityMapper;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPostgresqlAdapter implements UserOutputPort {
    private final UserRepository repository;
    private final UserEntityMapper mapper;

    @Override
    public User save(User user) {
        return mapper.toDomain(repository.save(mapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return mapper.toDomainList(repository.findAll());
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
