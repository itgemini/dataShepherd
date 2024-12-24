/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.model;

import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;

abstract class Draw extends Creation {
    protected Sheet sheet;
    protected Drawing<?> drawing;
    Draw(Sheet sheet){
        super(sheet.getWorkbook());
        drawing = sheet.createDrawingPatriarch();
    }

}
