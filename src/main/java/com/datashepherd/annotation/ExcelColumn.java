/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.annotation;


import com.datashepherd.annotation.style.ExcelStyle;
import com.datashepherd.annotation.style.Font;
import com.datashepherd.enums.Color;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to mark a field as a column in an Excel sheet.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {
    /**
     * Specifies the name of the Excel column.
     */
    String name() default "";

    /**
     * Specifies the starting position of the column in the Excel sheet.
     */
    int position() default 0;

    /**
     * Specifies the format of the column in the Excel sheet.
     * use {@link com.datashepherd.enums.PercentageFormat} {@link com.datashepherd.enums.DateFormat} {@link com.datashepherd.enums.CurrencyFormat} to specify the format.
     */
    String format() default "";

    ExcelStyle headerStyle() default @ExcelStyle(font = @Font(color = Color.BLACK,fontHeightInPoints = 11),backgroundColor = Color.WHITE,patternType = FillPatternType.NO_FILL);
}
