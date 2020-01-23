package com.okatu.rgan.blog;

public class BlogNotFoundException extends RuntimeException {
    public BlogNotFoundException(Long id) {
        super("Could not find blog" + id);
    }
}

