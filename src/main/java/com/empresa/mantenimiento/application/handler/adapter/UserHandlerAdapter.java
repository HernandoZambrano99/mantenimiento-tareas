package com.empresa.mantenimiento.application.handler.adapter;

import com.empresa.mantenimiento.application.handler.UserHandler;
import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.domain.usecase.input.UserInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserHandlerAdapter implements UserHandler {
    private final UserInputPort userInputPort;

    @Override
    @Transactional
    public User create(User user) {
        return userInputPort.create(user);
    }

    @Override
    @Transactional
    public User update(Long id, User user) {
        return userInputPort.update(id, user);
    }

    @Override
    public User findById(Long id) {
        return userInputPort.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userInputPort.findAll();
    }

    @Override
    @Transactional
    public void toggleActive(Long id) {
        userInputPort.toggleActive(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userInputPort.delete(id);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String currentPassword, String newPassword) {
        userInputPort.changePassword(id, currentPassword, newPassword);
    }
}
