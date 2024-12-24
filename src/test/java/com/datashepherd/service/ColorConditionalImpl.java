/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.service;

import com.datashepherd.enums.Color;
import com.datashepherd.helper.writer.style.condional.ColorCondition;

import java.util.Objects;

public class ColorConditionalImpl implements ColorCondition {
    @Override
    public <T> Color applyCondition(T fieldValue) {
        return Objects.nonNull(fieldValue)? Color.WHITE : Color.BLACK;
    }
}
