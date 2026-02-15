package com.datashepherd.pdf.helper;

import com.datashepherd.pdf.enums.PdfFieldType;

public class FieldExtractorFactory {
    private FieldExtractorFactory() {
    }

    public static FieldExtractor getExtractor(PdfFieldType type) {
        return switch (type) {
            case TEXT -> new TextFieldExtractor();
            case MULTILINE_LIST -> new MultiLineListFieldExtractor();
        };
    }
}
