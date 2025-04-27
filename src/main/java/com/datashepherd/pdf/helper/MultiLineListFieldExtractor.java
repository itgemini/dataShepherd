package com.datashepherd.pdf.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MultiLineListFieldExtractor implements FieldExtractor {

    @Override
    public Object extract(String content, String label) {
        List<String> results = new ArrayList<>();
        if (content == null || content.isEmpty() || label == null || label.isEmpty()) {
            return results;
        }

        int idx = content.indexOf(label);
        if (idx < 0) {
            return results;
        }

        int start = idx + label.length();

        while (start < content.length()) {
            char c = content.charAt(start);
            if (c == ':' || Character.isWhitespace(c)) {
                start++;
            } else {
                break;
            }
        }

        String remaining = content.substring(start);
        line(remaining, results);
        return results;
    }

    private void line(String remaining, List<String> results) {
        Scanner scanner = new Scanner(remaining);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().stripTrailing();
            if (line.isBlank() || isPotentialNewLabel(line)) {
                break;
            }
            results.add(line.trim());
        }
        scanner.close();
    }

    /**
     * Heuristic to detect if a line might be a new label heading.
     * For example, if it starts with uppercase letters or a known prefix.
     * Adjust this as needed.
     */
    private boolean isPotentialNewLabel(String line) {
        return line.matches("^[A-Z].+");
    }
}

