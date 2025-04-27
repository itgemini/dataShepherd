/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.style.condional;

import com.datashepherd.excel.enums.Color;

public interface ColorCondition {
    <T> Color applyCondition(T fieldValue);
}
