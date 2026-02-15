/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.annotation.style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.datashepherd.excel.enums.Color;
import com.datashepherd.excel.enums.FontName;
import com.datashepherd.excel.enums.FontStyle;

/**
 * Font is an annotation used to define the font applyFormatAndStyles of a cell in an Excel sheet.
 * It includes font name, font applyFormatAndStyles, and font height in points.
 * Example usage:
 * {@code @Font(fontName = FontName.ARIAL, fontStyle = FontStyle.BOLD, fontHeightInPoints = 12)}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Font {
 /**
  * Defines the color of the cell.
  * Example usage: {@code color = Colors.BLUE}
  */
 Color color();
 /**
  * Defines the name of the font.
  * Default is FontName.CALIBRI.
  */
 FontName fontName() default FontName.CALIBRI;

 /**
  * Defines the applyFormatAndStyles of the font.
  * Default is FontStyle.NORMAL.
  */
 FontStyle fontStyle() default FontStyle.NORMAL;

 /**
  * Defines the height in points of the font.
  */
 short fontHeightInPoints();
}