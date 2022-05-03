package com.waterfogsw.gcccoffee.exception;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound() {
        super("No data found");
    }
}
