package com.datashepherd.pdf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.datashepherd.pdf.enums.PdfFieldType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PdfField {
    String value();

    PdfFieldType type() default PdfFieldType.TEXT;
}
