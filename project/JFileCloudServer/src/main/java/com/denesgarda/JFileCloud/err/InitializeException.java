package com.denesgarda.JFileCloud.err;

public class InitializeException extends RuntimeException {
    public InitializeException() {
        super("The server is unable to be initialized.");
    }
}
