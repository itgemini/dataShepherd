/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.enums;


public class DateFormat {
    private DateFormat() {}
    public static final String FULL_DATE_TIME = "dddd, MMMM d, yyyy h:mm AM/PM";
    public static final String SHORT_DATE = "M/d/yy";
    public static final String MEDIUM_DATE = "MMM d, yyyy";
    public static final String LONG_DATE = "MMMM d, yyyy";
    public static final String FULL_DATE = "dddd, MMMM d, yyyy";
    public static final String SHORT_TIME = "h:mm AM/PM";
    public static final String MEDIUM_TIME = "h:mm:ss AM/PM";
    public static final String ISO_DATE = "yyyy-MM-dd";
    public static final String ISO_TIME = "HH:mm:ss";
    public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
}
