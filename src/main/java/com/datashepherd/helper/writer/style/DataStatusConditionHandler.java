/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.style;

import com.datashepherd.helper.writer.style.condional.Conditional;
import com.datashepherd.helper.writer.style.condional.DataStatusCondition;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

public interface DataStatusConditionHandler extends Conditional {
    default <T> void applyStatus(DataStatusCondition conditionalStatus, T fieldValue, CellStyle style){
            // Apply the color condition
        switch (conditionalStatus.applyCondition(fieldValue)){
            case WARNING -> {
                style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            case ERROR -> {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            case SUCCESS -> {
                style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            case DEFAULT -> {
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
        }
    }
}
