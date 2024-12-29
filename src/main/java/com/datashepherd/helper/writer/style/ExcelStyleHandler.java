/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.style;

import com.datashepherd.annotation.style.ExcelStyle;
import com.datashepherd.exception.StyleException;
import com.datashepherd.helper.writer.model.FormatHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.util.Objects;

public interface ExcelStyleHandler extends StyleHandler , FontStyle , BackgroundColorStyle {
    default void createStyle(ExcelStyle excelStyle,CellStyle style,Font font) {
        if (Objects.nonNull(excelStyle.font())) applyFontStyle(style,excelStyle.font(), font);
        if (Objects.nonNull(excelStyle.backgroundColor())) applyBackgroundColorStyle(style,excelStyle.backgroundColor());
        style.setAlignment(excelStyle.horizontalAlignment());
        style.setVerticalAlignment(excelStyle.verticalAlignment());
        if (Objects.nonNull(excelStyle.patternType())) style.setFillPattern(excelStyle.patternType());
    }

    default void defaultStyle(Font font,CellStyle style){
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Arial");
        font.setBold(false);
        font.setItalic(false);
        font.setStrikeout(false);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
    }

    default void style(FormatHandler formatHandler, Workbook workbook, Field field, Cell cellC, String format) {
        if(workbook.getNumCellStyles()>=63999) return;
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        cellC.setCellStyle(style);
        CellStyle cellStyle = cellC.getCellStyle();
        try {
            if(StringUtils.isNotBlank(format)) cellStyle.setDataFormat(formatHandler.getFormat(format));
        } catch (Exception e) {
            throw new StyleException("Failed to apply format", e);
        }
        applyStyles(field,cellStyle,font);
    }

    default void applyStyles(Field field, CellStyle style, Font font) {
        if (field.isAnnotationPresent(ExcelStyle.class)) createStyle(field.getAnnotation(ExcelStyle.class), style, font);
    }
}
