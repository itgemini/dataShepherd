package com.datashepherd.pdf.annotation;/* === 1. Annotations === */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field that must be populated from a PDF **table column**. The mapping can be
 * either by the column *header* text (recommended) or a *zero‑based index* if header text
 * is unreliable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PdfColumn {
    String name() default "";

    int order() default -1;

    String pattern() default "";

    String format() default "";

    /**
     * Exact (trimmed, case‑insensitive) header label to match.
     */
    String header() default "";

    /**
     * Optional fallback index (0‑based) if header not found / duplicate headers.
     */
    int index() default -1;
}