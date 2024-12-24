/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.exception;


public class ReadException extends RuntimeException {
    public ReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadException(String message) {
        super(message);
    }
}
