/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.style;

import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import com.datashepherd.excel.annotation.style.Font;

interface FontStyle {

    default void applyFontStyle(CellStyle style, Font font, org.apache.poi.ss.usermodel.Font workbookFont) {
        workbookFont.setFontName(font.fontName().getName());
        if(font.fontHeightInPoints()!=0) workbookFont.setFontHeightInPoints(font.fontHeightInPoints());
        workbookFont.setColor(IndexedColors.valueOf(font.color().name().toUpperCase()).getIndex());

        List<com.datashepherd.excel.enums.FontStyle> fontStyles = Collections.singletonList(font.fontStyle());

        workbookFont.setBold(fontStyles.contains(com.datashepherd.excel.enums.FontStyle.BOLD));
        workbookFont.setItalic(fontStyles.contains(com.datashepherd.excel.enums.FontStyle.ITALIC));
        workbookFont.setStrikeout(fontStyles.contains(com.datashepherd.excel.enums.FontStyle.STRIKETHROUGH));
        if (fontStyles.contains(com.datashepherd.excel.enums.FontStyle.UNDERLINE)) {
            workbookFont.setUnderline(org.apache.poi.ss.usermodel.Font.U_SINGLE);
        }
        if (fontStyles.contains(com.datashepherd.excel.enums.FontStyle.DOUBLE_UNDERLINE)) {
            workbookFont.setUnderline(org.apache.poi.ss.usermodel.Font.U_DOUBLE);
        }
        style.setFont(workbookFont);
    }
}