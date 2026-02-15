/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.annotation;


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
     * Specifies the character that mark the end of the Excel sheet.
     * This is used to determine the end of the sheet when reading data.
     * For example, if the end character is "END", the reading process will stop when it encounters this character.
     * This is useful for sheets that have a dynamic number of rows.
     * Be sure to set the end character in the last row of the sheet.
     * And make sure that the end character is not used in any other cell in the sheet.
     * For example, the end character can be in the last row of the first column.
     */
    String endSheet() default "";

    /**
     * Defines how many lines should be skipped at the beginning when reading.
     */
    int skipHeader() default 0;

    int headerRow() default 0;

    /**
     * Specifies the path to the image.
     * Positioning is required, including start and end columns and rows.
     */
    Picture picture() default @Picture(path = "", startColumn = 0, startRow = 0, endColumn = 0, endRow = 0);
}
