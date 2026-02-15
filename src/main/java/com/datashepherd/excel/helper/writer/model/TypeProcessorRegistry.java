/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import com.datashepherd.excel.annotation.Image;
import com.datashepherd.excel.exception.StyleException;

/**
 * Registry for type-specific Excel cell processors.
 */
public final class TypeProcessorRegistry {

    public static final BiConsumer<Cell, Object> TEXT = (cell, value) -> cell.setCellValue((String) value);
    public static final BiConsumer<Cell, Object> INTEGER = (cell, value) -> cell.setCellValue((Integer) value);
    public static final BiConsumer<Cell, Object> DOUBLE = (cell, value) -> cell.setCellValue((Double) value);
    public static final BiConsumer<Cell, Object> FLOAT = (cell, value) -> cell.setCellValue((Float) value);
    public static final BiConsumer<Cell, Object> LONG = (cell, value) -> cell.setCellValue((Long) value);
    public static final BiConsumer<Cell, Object> BOOLEAN = (cell, value) -> cell.setCellValue((Boolean) value);
    public static final BiConsumer<Cell, Object> DATE = (cell, value) -> cell.setCellValue((Date) value);
    public static final BiConsumer<Cell, Object> LOCAL_DATE = (cell, value) -> cell.setCellValue((LocalDate) value);
    public static final BiConsumer<Cell, Object> LOCAL_DATE_TIME = (cell, value) -> cell.setCellValue((LocalDateTime) value);
    public static final BiConsumer<Cell, Object> IMAGE = (cell, value) -> {
        try {
            if (value instanceof Pair<?, ?> pair && pair.getLeft() instanceof Image img) {
                CellImageHandler.insertImage(cell, (byte[]) pair.getRight(), img);
            }
        }
        catch (Exception e) {
            throw new StyleException("Failed to insert image", e);
        }
    };
    private static final Map<String, BiConsumer<Cell, Object>> PROCESSORS = Map.ofEntries(
            Map.entry("java.lang.Integer", INTEGER), Map.entry("int", INTEGER),
            Map.entry("java.lang.Double", DOUBLE), Map.entry("double", DOUBLE),
            Map.entry("java.lang.Float", FLOAT), Map.entry("float", FLOAT),
            Map.entry("java.lang.Long", LONG), Map.entry("long", LONG),
            Map.entry("java.lang.Boolean", BOOLEAN), Map.entry("boolean", BOOLEAN),
            Map.entry("java.util.Date", DATE), Map.entry("date", DATE),
            Map.entry("java.time.LocalDate", LOCAL_DATE),
            Map.entry("java.time.LocalDateTime", LOCAL_DATE_TIME),
            Map.entry("java.lang.String", TEXT),
            Map.entry("java.lang.Byte[]", IMAGE), Map.entry("byte[]", IMAGE), Map.entry("[B", IMAGE)
    );

    private TypeProcessorRegistry() {
    }

    public static BiConsumer<Cell, Object> getProcessor(String typeName) {
        return PROCESSORS.get(typeName);
    }
}
