package com.waterfogsw.gcccoffee.common.exception;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound() {
        super("No data found");
    }
}
