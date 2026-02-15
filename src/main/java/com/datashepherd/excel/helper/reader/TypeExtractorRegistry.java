/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.reader;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Registry for type-specific Excel cell value extractors.
 */
public final class TypeExtractorRegistry {

    public static final Function<Cell, Optional<Object>> TEXT = cell -> {
        Object value = getValue(cell);
        return Objects.nonNull(value) ? Optional.of(String.valueOf(value)) : Optional.empty();
    };
    public static final Function<Cell, Optional<Object>> INTEGER = cell -> {
        Object value = getValue(cell);
        if (value instanceof Number num) return Optional.of(num.intValue());
        if (value instanceof String str) {
            try {
                return Optional.of(Integer.valueOf(str));
            }
            catch (NumberFormatException ignored) {
            }
        }
        return Optional.empty();
    };
    public static final Function<Cell, Optional<Object>> DOUBLE = cell -> {
        Object value = getValue(cell);
        if (value instanceof Number num) return Optional.of(num.doubleValue());
        if (value instanceof String str) {
            try {
                return Optional.of(Double.valueOf(str));
            }
            catch (NumberFormatException ignored) {
            }
        }
        return Optional.empty();
    };
    public static final Function<Cell, Optional<Object>> FLOAT = cell -> {
        Object value = getValue(cell);
        if (value instanceof Number num) return Optional.of(num.floatValue());
        if (value instanceof String str) {
            try {
                return Optional.of(Float.valueOf(str));
            }
            catch (NumberFormatException ignored) {
            }
        }
        return Optional.empty();
    };
    public static final Function<Cell, Optional<Object>> LONG = cell -> {
        Object value = getValue(cell);
        if (value instanceof Number num) return Optional.of(num.longValue());
        if (value instanceof String str) {
            try {
                return Optional.of(Long.valueOf(str));
            }
            catch (NumberFormatException ignored) {
            }
        }
        return Optional.empty();
    };
    public static final Function<Cell, Optional<Object>> BOOLEAN = cell -> {
        Object value = getValue(cell);
        if (value instanceof Boolean b) return Optional.of(b);
        if (value instanceof String str) return Optional.of(Boolean.valueOf(str));
        return Optional.empty();
    };
    public static final Function<Cell, Optional<Object>> DATE = cell -> {
        Object value = getValue(cell);
        if (value instanceof Number) return Optional.of(cell.getDateCellValue());
        if (value instanceof String str) {
            try {
                return Optional.of(Date.from(Instant.parse(str)));
            }
            catch (Exception ignored) {
            }
        }
        return Optional.empty();
    };
    public static final Function<Cell, Optional<Object>> LOCAL_DATE = cell -> {
        Object value = getValue(cell);
        if (value instanceof Number) return Optional.of(cell.getLocalDateTimeCellValue().toLocalDate());
        if (value instanceof String str) {
            try {
                return Optional.of(LocalDate.parse(str));
            }
            catch (Exception ignored) {
            }
        }
        return Optional.empty();
    };
    public static final Function<Cell, Optional<Object>> LOCAL_DATE_TIME = cell -> {
        Object value = getValue(cell);
        if (value instanceof Number) return Optional.of(cell.getLocalDateTimeCellValue());
        if (value instanceof String str) {
            try {
                return Optional.of(LocalDateTime.parse(str));
            }
            catch (Exception ignored) {
            }
        }
        return Optional.empty();
    };
    private static final Map<String, Function<Cell, Optional<Object>>> EXTRACTORS = Map.ofEntries(
            Map.entry("java.lang.Integer", INTEGER), Map.entry("int", INTEGER),
            Map.entry("java.lang.Double", DOUBLE), Map.entry("double", DOUBLE),
            Map.entry("java.lang.Float", FLOAT), Map.entry("float", FLOAT),
            Map.entry("java.lang.Long", LONG), Map.entry("long", LONG),
            Map.entry("java.lang.Boolean", BOOLEAN), Map.entry("boolean", BOOLEAN),
            Map.entry("java.util.Date", DATE),
            Map.entry("java.time.LocalDate", LOCAL_DATE),
            Map.entry("java.time.LocalDateTime", LOCAL_DATE_TIME),
            Map.entry("java.lang.String", TEXT)
    );

    private TypeExtractorRegistry() {
    }

    public static Function<Cell, Optional<Object>> getExtractor(String typeName) {
        return EXTRACTORS.get(typeName);
    }

    private static Object getValue(Cell cell) {
        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            default -> null;
        };
    }
}
