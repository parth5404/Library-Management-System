
package com.tcs.Library.error;

public class NoAuthorFoundException extends RuntimeException {

    public NoAuthorFoundException() {
        super();
    }

    public NoAuthorFoundException(String message) {
        super(message);
    }
}
