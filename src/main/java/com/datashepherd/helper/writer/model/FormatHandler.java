/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.model;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Objects;

public class FormatHandler extends Creation {
    private static FormatHandler instance = null;

    private FormatHandler(Workbook workbook) {
        super(workbook);
    }

    public static synchronized FormatHandler getInstance(Workbook workbook) {
        if (Objects.isNull(instance)) instance = new FormatHandler(workbook);
        return instance;
    }
    public short getFormat(String format) {
        return factory.createDataFormat().getFormat(format);
    }
}
