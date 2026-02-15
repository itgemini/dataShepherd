package com.datashepherd.excel.helper.writer.model;

import java.util.List;
import java.util.function.Function;

import com.datashepherd.excel.annotation.ExcelColumn;
import com.datashepherd.excel.annotation.Image;
import com.datashepherd.excel.annotation.ValidationComment;
import com.datashepherd.excel.annotation.ValidationStatus;
import com.datashepherd.excel.annotation.style.ConditionalExcelCellStyle;
import com.datashepherd.excel.annotation.style.ExcelStyle;

public record ExcelMetadata(
        Class<?> entityClass,
        List<FieldMetadata> columns,
        List<ChildMetadata> children
) {
}

record FieldMetadata(
        String fieldName,
        int order,
        String columnName,
        Function<Object, Object> getter,
        ExcelColumn columnAnnotation,
        ExcelStyle styleAnnotation,
        Image imageAnnotation,
        ConditionalExcelCellStyle conditionalStyleAnnotation,
        ValidationStatus validationStatusAnnotation,
        ValidationComment validationCommentAnnotation
) {
}

record ChildMetadata(
        String fieldName,
        Class<?> mappedBy,
        String referencedBy,
        Function<Object, Object> getter
) {
}
