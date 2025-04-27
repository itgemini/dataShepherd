package com.datashepherd.pdf.annotation;

import com.datashepherd.pdf.enums.PdfFieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PdfField {
    String value();

    PdfFieldType type() default PdfFieldType.TEXT;
}
