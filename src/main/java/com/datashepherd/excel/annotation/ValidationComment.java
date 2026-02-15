/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.datashepherd.excel.helper.writer.CellCommentCondition;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidationComment {
    /**
     * Specifies the comment information for the Excel column based on a condition.
     * @return The comment information for the column.
     */
    Class<? extends CellCommentCondition> comment();
}
