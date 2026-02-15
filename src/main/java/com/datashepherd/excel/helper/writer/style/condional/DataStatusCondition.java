/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.style.condional;


import com.datashepherd.excel.enums.Status;

public interface DataStatusCondition {
    <T> Status applyCondition(T fieldValue);
}
