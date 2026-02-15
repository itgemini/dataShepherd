/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import com.datashepherd.excel.helper.writer.style.condional.Conditional;
import com.datashepherd.excel.helper.writer.style.condional.DataStatusCondition;

public interface DataStatusConditionHandler extends Conditional {
    default <T> void applyStatus(DataStatusCondition conditionalStatus, T fieldValue, CellStyle style){
        style.setFillForegroundColor(switch (conditionalStatus.applyCondition(fieldValue)) {
            case WARNING -> IndexedColors.YELLOW.getIndex();
            case ERROR -> IndexedColors.RED.getIndex();
            case SUCCESS -> IndexedColors.GREEN.getIndex();
            case DEFAULT -> IndexedColors.WHITE.getIndex();
        });
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
}
