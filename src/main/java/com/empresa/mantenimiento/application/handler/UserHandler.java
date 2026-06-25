package com.empresa.mantenimiento.application.handler;

import com.empresa.mantenimiento.domain.model.user.User;

import java.util.List;

public interface UserHandler {
    User create(User user);
    User update(Long id, User user);
    User findById(Long id);
    List<User> findAll();
    void toggleActive(Long id);
    void delete(Long id);
    void changePassword(Long id, String currentPassword, String newPassword);
}
