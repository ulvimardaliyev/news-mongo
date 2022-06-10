package com.news.mongo.app.newsmongo.exception;


public class NewsNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5283051332948996598L;

    public NewsNotFoundException(String message) {
        super(message);
    }
}
