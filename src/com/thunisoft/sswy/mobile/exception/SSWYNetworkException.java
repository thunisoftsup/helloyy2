package com.thunisoft.sswy.mobile.exception;

public class SSWYNetworkException extends SSWYException {
    private static final long serialVersionUID = -1581760455928362333L;

    public SSWYNetworkException(String detailMessage) {
        super(detailMessage);
    }

    public SSWYNetworkException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
