package com.datashepherd.pdf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PdfZone {
    /**
     * The text to look for in the PDF lines that marks the start of the table, eg: the name of the first column.
     */
    String start() default "";

    /**
     * The text to look for in the PDF lines that marks the start of the table, eg: the name of the first column.
     */
    String end() default "";

    /**
     * How many lines to skip AFTER we find the table name line
     * before we start reading actual row data.
     * Default = 0 means parse data lines immediately.
     */
    int skipLines() default 0;
}
