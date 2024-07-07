package com.myleaderboard.leaderboard_service.exception;

public class GenericException extends RuntimeException{
    public GenericException(String message) {
        super(message);
    }
    public GenericException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
