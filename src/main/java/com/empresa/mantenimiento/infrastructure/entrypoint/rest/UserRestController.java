package com.empresa.mantenimiento.infrastructure.entrypoint.rest;

import com.empresa.mantenimiento.application.handler.UserHandler;
import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.request.UserRequest;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.response.UserResponse;
import com.empresa.mantenimiento.infrastructure.entrypoint.mapper.UserRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final UserHandler userHandler;
    private final UserRestMapper mapper;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        User created = userHandler.create(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(userHandler.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(mapper.toResponseList(userHandler.findAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        User updated = userHandler.update(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<Void> toggleActive(@PathVariable Long id) {
        userHandler.toggleActive(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userHandler.delete(id);
        return ResponseEntity.noContent().build();
    }
}
