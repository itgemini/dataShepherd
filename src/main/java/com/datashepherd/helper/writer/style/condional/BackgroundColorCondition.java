/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.style.condional;

import com.datashepherd.enums.Color;

public interface BackgroundColorCondition {
    <T> Color applyCondition(T fieldValue);
}
