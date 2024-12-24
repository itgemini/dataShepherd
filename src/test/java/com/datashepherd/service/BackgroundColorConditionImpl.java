/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.service;

import com.datashepherd.enums.Color;
import com.datashepherd.helper.writer.style.condional.BackgroundColorCondition;

import java.util.Objects;

public class BackgroundColorConditionImpl implements BackgroundColorCondition {
    @Override
    public <T> Color applyCondition(T fieldValue) {
        return Objects.nonNull(fieldValue)? Color.BLACK : Color.WHITE;
    }
}
