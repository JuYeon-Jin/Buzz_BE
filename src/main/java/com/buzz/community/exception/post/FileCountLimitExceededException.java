package com.buzz.community.exception.post;

public class FileCountLimitExceededException extends RuntimeException {
    public FileCountLimitExceededException(String message) {
        super(message);
    }
}
