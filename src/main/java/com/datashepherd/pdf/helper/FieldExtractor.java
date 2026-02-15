package com.datashepherd.pdf.helper;

public interface FieldExtractor {
    Object extract(String content, String label);
}
