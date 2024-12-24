/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Cell {
    int row() default 0;
    int column() default 0;
    int firstRow() default 0;
    int lastRow() default 0;
    int firstColumn() default 0;
    int lastColumn() default 0;
    /**
     * Specifies the format of the column in the Excel sheet.
     * use {@link com.datashepherd.enums.PercentageFormat} {@link com.datashepherd.enums.DateFormat} {@link com.datashepherd.enums.CurrencyFormat} to specify the format.
     */
    String format() default "";
}
