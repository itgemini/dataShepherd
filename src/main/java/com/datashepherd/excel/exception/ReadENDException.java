/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.exception;


public class ReadENDException extends RuntimeException {
    public ReadENDException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadENDException(String message) {
        super(message);
    }
}
