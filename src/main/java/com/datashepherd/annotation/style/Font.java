/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.annotation.style;

import com.datashepherd.enums.Color;
import com.datashepherd.enums.FontName;
import com.datashepherd.enums.FontStyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Font is an annotation used to define the font style of a cell in an Excel sheet.
 * It includes font name, font style, and font height in points.
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
  * Defines the style of the font.
  * Default is FontStyle.NORMAL.
  */
 FontStyle fontStyle() default FontStyle.NORMAL;

 /**
  * Defines the height in points of the font.
  */
 short fontHeightInPoints();
}