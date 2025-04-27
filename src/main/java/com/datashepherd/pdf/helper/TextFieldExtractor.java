package com.datashepherd.pdf.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFieldExtractor implements FieldExtractor {
    public Object extract(String content, String label) {
        Pattern pattern = Pattern.compile(Pattern.quote(label) + "\\s*(.*)");
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(1).trim() : null;
    }
}