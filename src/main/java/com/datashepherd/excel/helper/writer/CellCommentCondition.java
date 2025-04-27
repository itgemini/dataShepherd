/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer;

public interface CellCommentCondition {
    <T> String applyCondition(T fieldValue);
}
