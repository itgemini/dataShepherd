/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.model;

import org.apache.poi.ss.usermodel.Workbook;

public class FormatHandler extends Creation {

    public FormatHandler(Workbook workbook) {
        super(workbook);
    }

    public short getFormat(String format) {
        return factory.createDataFormat().getFormat(format);
    }
}
