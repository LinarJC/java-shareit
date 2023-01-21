package ru.practicum.shareit.exception;

public class EmailValidateException extends RuntimeException {
    public EmailValidateException(String message) {
        super(message);
    }
}