/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.style.condional;


import com.datashepherd.enums.Status;

public interface DataStatusCondition {
    <T> Status applyCondition(T fieldValue);
}
