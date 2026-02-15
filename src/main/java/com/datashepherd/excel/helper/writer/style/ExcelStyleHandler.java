/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.style;


/**
 * ExcelStyleHandler provides default methods for applying styles and formats to Excel cells.
 * It integrates font, background color, and alignment styles based on @ExcelStyle annotations.
 */
public interface ExcelStyleHandler extends StyleHandler , FontStyle , BackgroundColorStyle {

}
