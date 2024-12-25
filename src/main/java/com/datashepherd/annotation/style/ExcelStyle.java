/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.annotation.style;

import com.datashepherd.enums.Color;
import org.apache.poi.ss.usermodel.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ExcelStyle is an annotation used to define the style of a cell or column in an Excel sheet.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelStyle {
    /**
     * Defines the font of the cell.
     * Example usage: {@code font = @Font(fontName = "Arial", fontHeightInPoints = 10)}
     */
    Font font();
    /**
     * Defines the background color of the cell.
     * Example usage: {@code backgroundColor = Colors.YELLOW}
     */
    Color backgroundColor();
    /**
     * Defines the horizontal alignment of the cell.
     * Example usage: {@code horizontalAlignment = HorizontalAlignment.CENTER}
     */
    HorizontalAlignment horizontalAlignment() default HorizontalAlignment.CENTER;
    /**
     * Defines the vertical alignment of the cell.
     * Example usage: {@code verticalAlignment = VerticalAlignment.CENTER}
     */
    VerticalAlignment verticalAlignment() default VerticalAlignment.CENTER;
    /**
     * Defines the fill pattern of the cell.
     * Example usage: {@code patternType = FillPatternType.SOLID_FOREGROUND}
     */
    FillPatternType patternType() default FillPatternType.SOLID_FOREGROUND;
}