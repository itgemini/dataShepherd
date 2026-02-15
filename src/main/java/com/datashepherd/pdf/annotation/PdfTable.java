package com.datashepherd.pdf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field that will receive the **List** of row objects parsed from the table.
 * Example usage:
 *
 * <pre>
 *     PdfTable(startMarker = "Sortie", rowType = CrcRow.class)
 *     private List<CrcRow> rows;
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface PdfTable {
    /**
     * A unique string that appears *on the header line* (e.g. "Sortie").
     */
    String startKeyword() default "";

    /**
     * Class representing one row of the table. Must have a no‑arg ctor.
     */
    String endKeyword() default "";

    int startPage() default 0;

    int endPage() default Integer.MAX_VALUE;

    Class<?> rowType();

    /**
     * Optional number of header lines to merge.
     * 1 = a single line header (default),
     * 2 = merge first two physical lines, etc.
     */
    int headerLines() default 1;
}