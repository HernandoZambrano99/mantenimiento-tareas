package com.empresa.mantenimiento.domain.usecase.input;

import com.empresa.mantenimiento.domain.model.user.User;

public interface AuthInputPort {

    /**
     * Verifies credentials and enforces the account-lockout policy. On a wrong
     * password it records the failed attempt (locking the account once the
     * threshold is reached); on success it clears any previous failed attempts.
     *
     * @return the authenticated user
     */
    User authenticate(String username, String rawPassword);

    /**
     * Loads a user (e.g. for a token refresh) and ensures the account is still
     * active. Throws if the user does not exist or has been disabled.
     */
    User validateActiveUser(String username);
}
