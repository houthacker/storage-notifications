package io.houthacker.notifications.exceptions;

/**
 * @author houthacker
 */
public class BackendException extends Exception {

    public BackendException() {
        super();
    }

    public BackendException(String message) {
        super(message);
    }


    public BackendException(String message, Throwable cause) {
        super(message, cause);
    }
}
