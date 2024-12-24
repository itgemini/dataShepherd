/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.exception;


public class WorkbookException extends RuntimeException {
    public WorkbookException(String message,Throwable throwable){
        super(message,throwable);
    }
    public WorkbookException(String message){
        super(message);
    }
}
