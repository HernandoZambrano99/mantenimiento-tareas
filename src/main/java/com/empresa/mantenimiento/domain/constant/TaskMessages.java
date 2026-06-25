package com.empresa.mantenimiento.domain.constant;

public final class TaskMessages {
    public static final int TITLE_MAX_LENGTH = 150;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final String ID_REQUIRED = "Task id is required";
    public static final String NOT_FOUND = "Task not found";
    public static final String TITLE_REQUIRED = "Title is required";
    public static final String TITLE_MAX_LENGTH_MESSAGE = "Title must be at most 150 characters";
    public static final String DESCRIPTION_MAX_LENGTH_MESSAGE = "Description must be at most 1000 characters";
    public static final String DUE_DATE_REQUIRED = "Due date is required";
    public static final String DUE_DATE_PAST = "Due date cannot be in the past";
    public static final String DUE_DATE_NOT_PAST = "Due date must be today or in the future";
    public static final String STATUS_REQUIRED = "Status is required";
    public static final String ASSIGNEE_REQUIRED_POSITIVE = "Assigned user id must be positive";
    public static final String ASSIGNEE_NOT_FOUND = "Assigned user not found";
    public static final String FORBIDDEN = "You are not allowed to access this task";

    private TaskMessages() {
    }
}
