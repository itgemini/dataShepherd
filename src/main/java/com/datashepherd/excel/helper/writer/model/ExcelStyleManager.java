package com.datashepherd.excel.helper.writer.model;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import com.datashepherd.excel.enums.FontStyle;
import com.datashepherd.excel.helper.writer.style.ConditionalCellStyleHandler;
import com.datashepherd.excel.helper.writer.style.DataStatusConditionHandler;
import com.datashepherd.excel.helper.writer.style.ExcelStyleHandler;

public class ExcelStyleManager implements ExcelStyleHandler, ConditionalCellStyleHandler, DataStatusConditionHandler {
    private final Workbook workbook;
    private final Map<StyleKey, CellStyle> styleCache = new ConcurrentHashMap<>();
    private final Map<FontKey, Font> fontCache = new ConcurrentHashMap<>();

    public ExcelStyleManager(Workbook workbook) {
        this.workbook = workbook;
    }

    public short getFormat(String format) {
        return workbook.getCreationHelper().createDataFormat().getFormat(format);
    }

    public CellStyle getOrCreateStyle(com.datashepherd.excel.annotation.style.ExcelStyle styleAnnotation, String format) {
        short formatIndex = StringUtils.isNotBlank(format) ? getFormat(format) : -1;
        StyleKey key = new StyleKey(styleAnnotation, format);
        return styleCache.computeIfAbsent(key, k -> {
            if (workbook.getNumCellStyles() >= 63999) {
                return styleCache.values().iterator().next(); // Extreme fallback
            }
            CellStyle style = workbook.createCellStyle();
            if (Objects.nonNull(styleAnnotation)) {
                Font font = getOrCreateFont(styleAnnotation.font());
                if (Objects.nonNull(font)) style.setFont(font);
                if (Objects.nonNull(styleAnnotation.backgroundColor())) {
                    applyBackgroundColorStyle(style, styleAnnotation.backgroundColor());
                }
                style.setAlignment(styleAnnotation.horizontalAlignment());
                style.setVerticalAlignment(styleAnnotation.verticalAlignment());
                if (Objects.nonNull(styleAnnotation.patternType())) {
                    style.setFillPattern(styleAnnotation.patternType());
                }
            }
            if (formatIndex != -1) {
                style.setDataFormat(formatIndex);
            }
            return style;
        });
    }

    public Font getOrCreateFont(com.datashepherd.excel.annotation.style.Font fontAnnotation) {
        if (Objects.isNull(fontAnnotation)) return null;
        FontKey key = new FontKey(fontAnnotation);
        return fontCache.computeIfAbsent(key, k -> {
            Font font = workbook.createFont();
            font.setFontName(fontAnnotation.fontName().getName());
            if (fontAnnotation.fontHeightInPoints() != 0) {
                font.setFontHeightInPoints(fontAnnotation.fontHeightInPoints());
            }
            font.setColor(IndexedColors.valueOf(fontAnnotation.color().name().toUpperCase()).getIndex());

            FontStyle fs = fontAnnotation.fontStyle();
            font.setBold(fs == FontStyle.BOLD);
            font.setItalic(fs == FontStyle.ITALIC);
            font.setStrikeout(fs == FontStyle.STRIKETHROUGH);
            if (fs == FontStyle.UNDERLINE) font.setUnderline(Font.U_SINGLE);
            if (fs == FontStyle.DOUBLE_UNDERLINE) font.setUnderline(Font.U_DOUBLE);

            return font;
        });
    }

    public CellStyle getOrCreateStatusStyle(com.datashepherd.excel.helper.writer.style.condional.DataStatusCondition condition, Object value) {
        String statusName = condition.applyCondition(value).name();
        StyleKey key = new StyleKey(null, "STATUS_" + statusName);
        return styleCache.computeIfAbsent(key, k -> {
            if (workbook.getNumCellStyles() >= 63999) {
                return styleCache.isEmpty() ? workbook.createCellStyle() : styleCache.values().iterator()
                                                                                     .next();
            }
            CellStyle style = workbook.createCellStyle();
            applyStatus(condition, value, style);
            return style;
        });
    }

    public CellStyle getOrCreateConditionalStyle(com.datashepherd.excel.helper.writer.style.condional.ColorCondition colorCondition, com.datashepherd.excel.helper.writer.style.condional.BackgroundColorCondition backgroundColorCondition, Object value) {
        String keySuffix = (Objects.nonNull(colorCondition) ? "COLOR_" + colorCondition.applyCondition(value)
                                                                                       .name() : "") +
                (Objects.nonNull(backgroundColorCondition) ? "_BG_" + backgroundColorCondition.applyCondition(value)
                                                                                              .name() : "");
        StyleKey key = new StyleKey(null, "CONDITIONAL_" + keySuffix);
        return styleCache.computeIfAbsent(key, k -> {
            if (workbook.getNumCellStyles() >= 63999) {
                return styleCache.isEmpty() ? workbook.createCellStyle() : styleCache.values().iterator()
                                                                                     .next();
            }
            CellStyle style = workbook.createCellStyle();
            if (Objects.nonNull(colorCondition)) {
                Font font = workbook.createFont();
                createConditionalCellStyle(colorCondition, value, font);
                style.setFont(font);
            }
            if (Objects.nonNull(backgroundColorCondition)) {
                createConditionalCellStyle(backgroundColorCondition, value, style);
            }
            return style;
        });
    }

    private record StyleKey(com.datashepherd.excel.annotation.style.ExcelStyle annotation, String format) {
    }

    private record FontKey(com.datashepherd.excel.annotation.style.Font annotation) {
    }
}
