/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.annotation;


import com.datashepherd.helper.writer.style.condional.DataStatusCondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidationStatus {
    /**
     * Specifies the status information for the Excel column based on a condition.
     * @return The status information for the column.
     */
    Class<? extends DataStatusCondition> status();
}
