package org.ks365.osmp.common.exception;

public class SessionExpireException extends Exception {
    private static final long serialVersionUID = 2502346016803518913L;

    public SessionExpireException() {
    }

    public SessionExpireException(String message) {
        super(message);
    }
}
