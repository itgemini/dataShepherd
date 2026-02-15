/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import com.datashepherd.excel.enums.Color;

interface BackgroundColorStyle {
    default void applyBackgroundColorStyle(CellStyle style,Color color) {
        short colorIndex = IndexedColors.valueOf(color.name().toUpperCase()).getIndex();
        style.setFillForegroundColor(colorIndex);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
}