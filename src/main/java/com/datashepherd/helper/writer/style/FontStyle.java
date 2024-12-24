/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.style;

import com.datashepherd.annotation.style.Font;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Collections;
import java.util.List;

interface FontStyle {

    default void applyFontStyle(CellStyle style, Font font, org.apache.poi.ss.usermodel.Font workbookFont) {
        workbookFont.setFontName(font.fontName().getName());
        if(font.fontHeightInPoints()!=0) workbookFont.setFontHeightInPoints(font.fontHeightInPoints());
        workbookFont.setColor(IndexedColors.valueOf(font.color().name().toUpperCase()).getIndex());

        List<com.datashepherd.enums.FontStyle> fontStyles = Collections.singletonList(font.fontStyle());

        workbookFont.setBold(fontStyles.contains(com.datashepherd.enums.FontStyle.BOLD));
        workbookFont.setItalic(fontStyles.contains(com.datashepherd.enums.FontStyle.ITALIC));
        workbookFont.setStrikeout(fontStyles.contains(com.datashepherd.enums.FontStyle.STRIKETHROUGH));
        if (fontStyles.contains(com.datashepherd.enums.FontStyle.UNDERLINE)) {
            workbookFont.setUnderline(org.apache.poi.ss.usermodel.Font.U_SINGLE);
        }
        if (fontStyles.contains(com.datashepherd.enums.FontStyle.DOUBLE_UNDERLINE)) {
            workbookFont.setUnderline(org.apache.poi.ss.usermodel.Font.U_DOUBLE);
        }
        style.setFont(workbookFont);
    }
}