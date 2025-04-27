package com.datashepherd.pdf.helper;

import com.datashepherd.pdf.annotation.PdfColumn;
import com.datashepherd.pdf.annotation.PdfTable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PdfTableExtractor {
    public static <T> List<T> extract(String pdfText, Class<T> targetClass) throws IOException {
        List<T> result = new ArrayList<>();
        PdfTable tableAnnotation = targetClass.getAnnotation(PdfTable.class);

        if (tableAnnotation == null) {
            throw new IllegalArgumentException("Target class must be annotated with @PdfTable");
        }

        // Get all fields with @PdfColumn annotation
        Map<Integer, Field> orderedFields = Arrays.stream(targetClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(PdfColumn.class))
                .collect(Collectors.toMap(
                        f -> f.getAnnotation(PdfColumn.class).order(),
                        f -> f,
                        (f1, f2) -> f1, TreeMap::new
                ));

        // Find table content (simplified - you'll need more sophisticated logic)
        String[] lines = pdfText.split("\\r?\\n");
        boolean inTable = false;

        for (String line : lines) {
            if (!inTable && tableAnnotation.startKeyword().isEmpty() || line.contains(tableAnnotation.startKeyword())) {
                inTable = true;
                continue;
            }

            if (inTable && !tableAnnotation.endKeyword().isEmpty() && line.contains(tableAnnotation.endKeyword())) {
                break;
            }

            if (inTable) {
                T rowObject = parseLineToObject(line, targetClass, orderedFields);
                if (rowObject != null) {
                    result.add(rowObject);
                }
            }
        }

        return result;
    }

    private static <T> T parseLineToObject(String line, Class<T> targetClass, Map<Integer, Field> orderedFields) {
        try {
            T instance = targetClass.getDeclaredConstructor().newInstance();
            String[] values = line.split("\\s{2,}"); // Split by multiple whitespaces

            int i = 0;
            for (Map.Entry<Integer, Field> entry : orderedFields.entrySet()) {
                Field field = entry.getValue();
                PdfColumn columnAnnotation = field.getAnnotation(PdfColumn.class);

                if (i < values.length) {
                    field.setAccessible(true);
                    String value = values[i].trim();

                    // Convert value to field type
                    Object convertedValue = convertValue(value, field.getType(), columnAnnotation);
                    field.set(instance, convertedValue);
                }
                i++;
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object convertValue(String value, Class<?> targetType, PdfColumn columnAnnotation) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            if (targetType == String.class) {
                return value;
            } else if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(value.replaceAll("[^0-9]", ""));
            } else if (targetType == Double.class || targetType == double.class) {
                return Double.parseDouble(value.replaceAll("[^0-9.]", ""));
            } else if (targetType == LocalDate.class) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(columnAnnotation.format());
                return LocalDate.parse(value, formatter);
            }
            // Add more type conversions as needed
        } catch (Exception e) {
            System.err.println("Error converting value: " + value + " to " + targetType);
        }

        return null;
    }
}