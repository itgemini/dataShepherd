package com.datashepherd.pdf.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.datashepherd.pdf.PDFException;
import com.datashepherd.pdf.annotation.PdfField;
import com.datashepherd.pdf.annotation.PdfTable;
import com.datashepherd.pdf.annotation.PdfZone;

public class PdfObjectMapper {
    private PdfObjectMapper() {
    }

    public static <T> T map(String pdfContent, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                if (field.isAnnotationPresent(PdfField.class)) {
                    PdfField annotation = field.getAnnotation(PdfField.class);
                    FieldExtractor extractor = FieldExtractorFactory.getExtractor(Objects.requireNonNull(annotation).type());
                    Object rawValue = extractor.extract(pdfContent, annotation.value());
                    Object convertedValue = convertType(field.getType(), rawValue);
                    Method setter = clazz.getMethod("set" + capitalize(field.getName()), field.getType());
                    setter.invoke(instance, convertedValue);
                } else if (field.isAnnotationPresent(PdfZone.class)) {
                    PdfZone table = field.getAnnotation(PdfZone.class);
                    ZoneFieldExtractor tableExtractor = new ZoneFieldExtractor(Objects.requireNonNull(table).skipLines());
                    List<String> tableObjects = tableExtractor.extract(pdfContent, table.start(), table.end());
                    Method setter = clazz.getMethod("set" + capitalize(field.getName()), field.getType());
                    setter.invoke(instance, tableObjects);
                } else if (field.isAnnotationPresent(PdfTable.class)) {
                    PdfTable ann = field.getAnnotation(PdfTable.class);
                    List<?> rows = PdfTableExtractor.extract(
                            pdfContent,
                            Objects.requireNonNull(ann).rowType());
                    Method setter = clazz.getMethod("set" + capitalize(field.getName()), field.getType());
                    setter.invoke(instance, rows);
                }
            }
            return instance;
        } catch (Exception e) {
            throw new PDFException("PDF mapping failed", e);
        }
    }

    /**
     * Convert raw data (string or list) to the correct field type.
     */
    private static Object convertType(Class<?> targetType, Object rawValue) {
        if (rawValue == null) return null;

        if (targetType.isInstance(rawValue)) {
            return rawValue;
        }

        if (isCollectionType(targetType, rawValue)) {
            return rawValue;
        }

        if (rawValue instanceof String str) {
            return convertFromString(targetType, str);
        }

        return rawValue;
    }

    private static boolean isCollectionType(Class<?> targetType, Object rawValue) {
        return Collection.class.isAssignableFrom(targetType) && rawValue instanceof Collection<?>;
    }

    private static Object convertFromString(Class<?> targetType, String str) {
        if (targetType == String.class) return str;
        if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(str);
        if (targetType == double.class || targetType == Double.class) return Double.parseDouble(str);
        if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(str);
        return null;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}