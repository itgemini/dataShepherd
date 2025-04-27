package com.datashepherd.pdf.helper;

import com.datashepherd.pdf.annotation.PdfColumn;
import com.datashepherd.pdf.annotation.PdfTable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SmartTableExtractor {

    private SmartTableExtractor() {
    }


    public static List<?> parseTable(String content, PdfTable ann) {
        // 1) locate start of table (header line index)
        String[] lines = content.split("\\R"); // split by any newline seq
        int headerIdx = -1;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toLowerCase().contains(ann.startKeyword().toLowerCase())) {
                headerIdx = i;
                break;
            }
        }
        if (headerIdx == -1) return Collections.emptyList();

        // 2) merge header lines if needed
        StringBuilder headerLine = new StringBuilder(lines[headerIdx]);
        for (int h = 1; h < ann.headerLines() && headerIdx + h < lines.length; h++) {
            headerLine.append(" ").append(lines[headerIdx + h]);
        }

        // 3) determine column start positions based on runs of >=2 spaces (delimiter‑agnostic)
        int[] colStarts = detectColumnStarts(headerLine.toString());

        // 4) normalize header titles by slicing headerLine using colStarts
        List<String> headers = sliceLine(headerLine.toString(), colStarts);

        // 5) build row objects until we hit a blank line or next section
        List<Object> rows = new ArrayList<>();
        for (int i = headerIdx + ann.headerLines(); i < lines.length; i++) {
            String ln = lines[i];
            if (ln.trim().isEmpty()) break; // blank line => table ended
            List<String> cells = sliceLine(ln, colStarts);
            // defensive: skip decorative separators etc.
            if (emptyOrDecorative(cells)) continue;
            Object row = mapCellsToRow(headers, cells, ann.rowType());
            rows.add(row);
        }
        return rows;
    }

    /* --- util helpers --- */

    private static int[] detectColumnStarts(String header) {
        List<Integer> starts = new ArrayList<>();
        starts.add(0);
        for (int i = 1; i < header.length() - 1; i++) {
            if (header.charAt(i) == ' ' && header.charAt(i + 1) == ' ') {
                // run of >=2 spaces → potential new column start after the spaces
                while (i < header.length() && header.charAt(i) == ' ') i++;
                starts.add(i);
            }
        }
        // add sentinel end position
        starts.add(header.length());
        return starts.stream().mapToInt(Integer::intValue).toArray();
    }

    private static List<String> sliceLine(String line, int[] starts) {
        List<String> cells = new ArrayList<>(starts.length - 1);
        for (int c = 0; c < starts.length - 1; c++) {
            int begin = starts[c];
            int end = starts[c + 1];
            if (begin >= line.length()) {
                cells.add("");
            } else {
                int realEnd = Math.min(end, line.length());
                cells.add(line.substring(begin, realEnd).trim());
            }
        }
        return cells;
    }

    private static boolean emptyOrDecorative(List<String> cells) {
        long nonEmpty = cells.stream().filter(s -> !s.isEmpty()).count();
        return nonEmpty == 0 || cells.get(0).startsWith("---");
    }

    private static Object mapCellsToRow(List<String> headers, List<String> cells, Class<?> rowType) {
        try {
            Object row = rowType.getDeclaredConstructor().newInstance();
            Field[] fields = rowType.getDeclaredFields();
            for (Field f : fields) {
                PdfColumn colAnn = f.getAnnotation(PdfColumn.class);
                if (colAnn == null) continue;

                int idx = -1;
                if (!colAnn.header().isEmpty()) {
                    idx = indexOfHeader(headers, colAnn.header());
                }
                if (idx == -1 && colAnn.index() >= 0) {
                    idx = colAnn.index();
                }
                if (idx == -1 || idx >= cells.size()) continue; // no matching column

                String raw = cells.get(idx);
                Object value = convert(raw, f.getType());
                setField(row, f, value);
            }
            return row;
        } catch (Exception e) {
            throw new RuntimeException("Row mapping failed", e);
        }
    }

    private static int indexOfHeader(List<String> hdrs, String target) {
        for (int i = 0; i < hdrs.size(); i++) {
            if (hdrs.get(i).equalsIgnoreCase(target)) return i;
        }
        return -1;
    }

    private static Object convert(String raw, Class<?> type) {
        if (raw == null || raw.isBlank() || raw.equalsIgnoreCase("x")) return null;
        try {
            return switch (type.getName()) {
                case "java.lang.Integer", "int" -> Integer.parseInt(raw);
                case "java.lang.Long", "long" -> Long.parseLong(raw);
                case "java.lang.Double", "double" -> Double.parseDouble(raw);
                case "java.lang.Float", "float" -> Float.parseFloat(raw);
                case "java.lang.Boolean", "boolean" -> Boolean.parseBoolean(raw);
                default -> raw; // fallback to String
            };
        } catch (NumberFormatException ex) {
            return null; // malformed numbers become null safely
        }
    }

    private static void setField(Object obj, Field field, Object value) {
        try {
            Method setter = obj.getClass().getMethod(
                    "set" + capitalize(field.getName()), field.getType());
            setter.invoke(obj, value);
        } catch (Exception e) {
            // ignore missing setter; try direct access
            try {
                field.setAccessible(true);
                field.set(obj, value);
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
