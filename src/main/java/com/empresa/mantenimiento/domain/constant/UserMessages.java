package com.empresa.mantenimiento.domain.constant;

public final class UserMessages {
    public static final int FIRST_NAME_MAX_LENGTH = 100;
    public static final int LAST_NAME_MAX_LENGTH = 100;
    public static final int EMAIL_MAX_LENGTH = 150;
    public static final int USERNAME_MAX_LENGTH = 50;
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int MAX_FAILED_ATTEMPTS = 5;
    public static final int LOCK_DURATION_MINUTES = 15;

    public static final String ID_REQUIRED = "User id is required";
    public static final String NOT_FOUND = "User not found";
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String FIRST_NAME_MAX_LENGTH_MESSAGE = "First name must be at most 100 characters";
    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String LAST_NAME_MAX_LENGTH_MESSAGE = "Last name must be at most 100 characters";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Email format is invalid";
    public static final String EMAIL_MAX_LENGTH_MESSAGE = "Email must be at most 150 characters";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String USERNAME_MIN_LENGTH_MESSAGE = "Username must be at least 3 characters";
    public static final String USERNAME_MAX_LENGTH_MESSAGE = "Username must be at most 50 characters";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_MIN_LENGTH_MESSAGE = "Password must be at least 8 characters";
    public static final String PASSWORD_MAX_LENGTH_MESSAGE = "Password must be at most 100 characters";
    public static final String ROLE_REQUIRED = "Role is required";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ACCOUNT_LOCKED = "Account is locked. Try again later";
    public static final String ACCOUNT_DISABLED = "Account is disabled";
    public static final String CURRENT_PASSWORD_INCORRECT = "Current password is incorrect";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_INVALID = "Invalid token";

    private UserMessages() {
    }
}
