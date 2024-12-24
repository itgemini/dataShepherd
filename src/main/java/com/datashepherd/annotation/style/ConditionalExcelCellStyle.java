/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.annotation.style;


import com.datashepherd.helper.writer.style.condional.BackgroundColorCondition;
import com.datashepherd.helper.writer.style.condional.ColorCondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ConditionalExcelCellStyle is an annotation used to define the conditional style of a cell in an Excel sheet based on a condition.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ConditionalExcelCellStyle {
    /**
     * Defines the color condition of the cell. Implement the ColorCondition interface to create a custom color condition.
     * Example of a ColorCondition implementation:
     * {@code
     * public class MyColorCondition implements ColorCondition {
     *     public IndexedColors applyCondition(Object fieldValue) {
     *         if ("Success".equals(fieldValue)) {
     *             return IndexedColors.GREEN;
     *         } else {
     *             return IndexedColors.RED;
     *         }
     *     }
     * }
     * }
     */
    Class<? extends ColorCondition> colorCondition();

    /**
     * Defines the background color condition of the cell. Implement the BackgroundColorCondition interface to create a custom background color condition.
     * Example of a BackgroundColorCondition implementation:
     * {@code
     * public class MyBackgroundColorCondition implements BackgroundColorCondition {
     *     public IndexedColors applyCondition(Object fieldValue) {
     *         if (fieldValue instanceof Number && ((Number) fieldValue).doubleValue() > 100) {
     *             return IndexedColors.LIGHT_YELLOW;
     *         } else {
     *             return IndexedColors.LIGHT_BLUE;
     *         }
     *     }
     * }
     * }
     */
    Class<? extends BackgroundColorCondition> backgroundColorCondition();
}
