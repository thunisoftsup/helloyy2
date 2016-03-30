package com.thunisoft.sswy.mobile.exception;

public class SSWYException extends Exception {
    private static final long serialVersionUID = 3751211979539402839L;

    public SSWYException(String detailMessage) {
        super(detailMessage);
    }

    public SSWYException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
