/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import com.datashepherd.excel.annotation.Child;
import com.datashepherd.excel.annotation.ExcelColumn;
import com.datashepherd.excel.annotation.Sheet;

/**
 * Helper class to extract metadata from annotated Excel entity classes.
 */
public final class ExcelMetadataReader {

    private ExcelMetadataReader() {
    }

    public static Sheet getSheetAnnotation(Class<?> clazz) {
        Sheet sheet = clazz.getAnnotation(Sheet.class);
        if (sheet == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with @Sheet");
        }
        return sheet;
    }

    public static Stream<Field> getExcelColumnFields(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                     .filter(field -> field.isAnnotationPresent(ExcelColumn.class));
    }

    public static Stream<Field> getChildFields(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                     .filter(field -> field.isAnnotationPresent(Child.class));
    }

    public static boolean hasAllDefaultPositions(Class<?> clazz) {
        return getExcelColumnFields(clazz)
                .allMatch(field -> field.getAnnotation(ExcelColumn.class).position() == 0);
    }
}
