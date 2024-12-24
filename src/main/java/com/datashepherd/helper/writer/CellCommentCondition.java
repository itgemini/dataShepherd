/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer;

public interface CellCommentCondition {
    <T> String applyCondition(T fieldValue);
}
