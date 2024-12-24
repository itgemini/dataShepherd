/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.service;

import com.datashepherd.helper.writer.CellCommentCondition;

public class CellCommentConditionImpl implements CellCommentCondition {
    @Override
    public String applyCondition(Object fieldValue) {
        return "This is a comment";
    }
}
