package com.datashepherd.pdf.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ZoneFieldExtractor that:
 * 1) Finds 'start and end' in the PDF text.
 * 2) Skips 'skipLines' lines after that.
 * 3) Then each non-blank line is considered a row, parsed by splitColumns().
 */
public class ZoneFieldExtractor {
    private final int skipLines;

    /**
     * return a list of lines from the PDF text
     */
    public ZoneFieldExtractor(int skipLines) {
        this.skipLines = skipLines;
    }

    public List<String> extract(String content, String start, String end) {
        List<String> rowObjects = new ArrayList<>();
        String[] lines = content.split("\\r?\\n");

        boolean foundStart = false;
        int linesToSkip = 0;

        for (String rawLine : lines) {
            String line = rawLine.trim();

            if ((StringUtils.isNoneBlank(start) && !foundStart) || linesToSkip > 0) {
                if (line.contains(start)) {
                    foundStart = true;
                    linesToSkip = skipLines;
                } else if (linesToSkip > 0) {
                    linesToSkip--;
                }
                continue;
            }
            if (line.isEmpty() || (StringUtils.isNoneBlank(end) && line.contains(end))) break;
            rowObjects.add(line);
        }

        return rowObjects;
    }
}