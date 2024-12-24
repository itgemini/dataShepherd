/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper;


public enum WorkbookType {
    XSSF("org.apache.poi.xssf.usermodel.XSSFWorkbook", "xlsx"),
    SXSSF("org.apache.poi.xssf.streaming.SXSSFWorkbook", "xlsx"),
    HSSF("org.apache.poi.hssf.usermodel.HSSFWorkbook", "xls");

    private final String className;
    private final String fileExtension;

    WorkbookType(String className, String fileExtension) {
        this.className = className;
        this.fileExtension = fileExtension;
    }

    public String getClassName() {
        return className;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
