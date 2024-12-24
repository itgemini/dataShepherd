/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.style;

import com.datashepherd.helper.writer.style.condional.BackgroundColorCondition;
import com.datashepherd.helper.writer.style.condional.ColorCondition;
import com.datashepherd.helper.writer.style.condional.Conditional;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Objects;

public interface ConditionalCellStyleHandler extends Conditional {
    default <T> void createConditionalCellStyle(ColorCondition colorCondition, T fieldValue, Font font) {
        if(Objects.nonNull(colorCondition)) {
            // Apply the color condition
            font.setColor(IndexedColors.valueOf(colorCondition.applyCondition(fieldValue).name().toUpperCase()).getIndex());
        }
    }

    default <T> void createConditionalCellStyle(BackgroundColorCondition backgroundColorCondition, T fieldValue, CellStyle style) {
        if(Objects.nonNull(backgroundColorCondition)){
            // Apply the background color condition
            style.setFillForegroundColor(IndexedColors.valueOf(backgroundColorCondition.applyCondition(fieldValue).name().toUpperCase()).getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
    }
}