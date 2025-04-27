package com.datashepherd.pdf;

public class PDFException extends RuntimeException {
    public PDFException(String message, Throwable cause) {
        super(message, cause);
    }

    public PDFException(String message) {
        super(message);
    }
}
