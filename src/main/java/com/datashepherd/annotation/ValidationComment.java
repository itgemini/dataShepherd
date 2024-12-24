/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.annotation;


import com.datashepherd.helper.writer.CellCommentCondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidationComment {
    /**
     * Specifies the comment information for the Excel column based on a condition.
     * @return The comment information for the column.
     */
    Class<? extends CellCommentCondition> comment();
}
