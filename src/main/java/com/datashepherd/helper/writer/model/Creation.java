/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.model;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;

abstract class Creation {
    protected CreationHelper factory;
    Creation(Workbook workbook){
        factory = workbook.getCreationHelper();
    }
}
