/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.model;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.datashepherd.excel.helper.writer.style.condional.Registry;

/**
 * Context object to hold common writing components and reduce parameter passing.
 */
public record WritingContext(
        Workbook workbook,
        Sheet sheet,
        Registry registry,
        FormatHandler formatHandler,
        ExcelStyleManager styleManager
) {
    public static WritingContext of(Workbook workbook, Sheet sheet, Registry registry) {
        return new WritingContext(workbook, sheet, registry, new FormatHandler(workbook), new ExcelStyleManager(workbook));
    }
}
