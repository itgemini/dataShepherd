/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a class as representing an Excel sheet.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sheet {

    /**
     * Specifies the name of the Excel sheet.
     */
    String name();

    /**
     * Specifies the header content of the Excel sheet.
     */
    String centerHeader() default "";

    String rightHeader() default "";

    String leftHeader() default "";

    /**
     * Specifies the footer content of the Excel sheet.
     */
    String centerFooter() default "";

    String rightFooter() default "";

    String leftFooter() default "";

    /**
     * Specifies the path to the image.
     * Positioning is required, including start and end columns and rows.
     */
    Picture picture() default @Picture(path = "", startColumn = 0, startRow = 0, endColumn = 0, endRow = 0);
}
