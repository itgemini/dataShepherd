/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.service;

import com.datashepherd.enums.Status;
import com.datashepherd.helper.writer.style.condional.DataStatusCondition;

import java.util.Objects;

public class DataStatusConditionImpl implements DataStatusCondition {
    @Override
    public <T> Status applyCondition(T fieldValue) {
        if(Objects.nonNull(fieldValue) && fieldValue instanceof Integer value){
            if(value > 30) return Status.SUCCESS;
            else if (value == 30) return Status.WARNING;
            else return Status.ERROR;
        }
        return Status.ERROR;
    }
}
