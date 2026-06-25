package com.empresa.mantenimiento.domain.usecase.input;

import com.empresa.mantenimiento.domain.model.user.User;

import java.util.List;

public interface UserInputPort {
    User create(User user);
    User update(Long id, User user);
    User findById(Long id);
    List<User> findAll();
    void toggleActive(Long id);
    void delete(Long id);
    void changePassword(Long id, String currentPassword, String newPassword);
}
