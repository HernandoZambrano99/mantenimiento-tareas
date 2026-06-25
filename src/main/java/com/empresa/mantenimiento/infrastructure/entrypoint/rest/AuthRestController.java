package com.empresa.mantenimiento.infrastructure.entrypoint.rest;

import com.empresa.mantenimiento.application.handler.AuthHandler;
import com.empresa.mantenimiento.application.handler.UserHandler;
import com.empresa.mantenimiento.domain.constant.UserMessages;
import com.empresa.mantenimiento.domain.exception.DomainUnauthorizedException;
import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.domain.model.user.gateway.UserOutputPort;
import com.empresa.mantenimiento.infrastructure.config.JwtService;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.request.ChangePasswordRequest;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.request.LoginRequest;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.request.RefreshTokenRequest;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth entrypoint. Pure orchestration: the authentication rules (credential
 * check, failed-attempt tracking, lockout, active-account check) live in
 * {@link AuthHandler} / the domain. This controller only owns JWT issuing,
 * which is an infrastructure concern.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final AuthHandler authHandler;
    private final UserHandler userHandler;
    private final UserOutputPort userOutputPort;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authHandler.authenticate(request.username(), request.password());
        return ResponseEntity.ok(buildLoginResponse(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String token = request.refreshToken();
        if (!jwtService.isValid(token) || !jwtService.isRefreshToken(token)) {
            throw new DomainUnauthorizedException(UserMessages.TOKEN_INVALID);
        }
        User user = authHandler.validateActiveUser(jwtService.extractUsername(token));
        return ResponseEntity.ok(buildLoginResponse(user));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new DomainUnauthorizedException(UserMessages.INVALID_CREDENTIALS);
        }
        User user = userOutputPort.findByUsername(auth.getName())
                .orElseThrow(() -> new DomainUnauthorizedException(UserMessages.NOT_FOUND));

        userHandler.changePassword(user.getId(), request.currentPassword(), request.newPassword());
        return ResponseEntity.noContent().build();
    }

    private LoginResponse buildLoginResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        return new LoginResponse(
                accessToken,
                refreshToken,
                jwtService.getExpirationMs() / 1000,
                user.getRole().name()
        );
    }
}
