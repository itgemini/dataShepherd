/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper;


import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static com.datashepherd.excel.helper.WorkbookType.SXSSF;

public class WorkbookFactory {
    static {
        // Set higher limit for large records in Apache POI (e.g. 512MB)
        IOUtils.setByteArrayMaxOverride(512 * 1024 * 1024);
    }
    private WorkbookFactory(){}
    public static Workbook createWorkbook(WorkbookType workbookType) throws ReflectiveOperationException {
        String className = workbookType.getClassName();
        Class<?> workbookClass = Class.forName(className);
        return (Workbook) workbookClass.getDeclaredConstructor().newInstance();
    }

    public static Workbook createWorkbook(WorkbookType workbookType, InputStream template) throws ReflectiveOperationException {
        String className = workbookType.getClassName();
        Class<?> workbookClass = Class.forName(className);
        return (Workbook) workbookClass.getConstructor(InputStream.class).newInstance(template);
    }

    public static Workbook createWorkbook(XSSFWorkbook xssfWorkbook) throws ReflectiveOperationException {
        String className = SXSSF.getClassName();
        Class<?> workbookClass = Class.forName(className);
        return (Workbook) workbookClass.getConstructor(XSSFWorkbook.class).newInstance(xssfWorkbook);
    }

    public static Workbook createWorkbook(XSSFWorkbook xssfWorkbook, int rowAccessWindowSize, boolean compressTmpFiles) throws ReflectiveOperationException {
        String className = SXSSF.getClassName();
        Class<?> workbookClass = Class.forName(className);
        return (Workbook) workbookClass.getConstructor(XSSFWorkbook.class,int.class,boolean.class).newInstance(xssfWorkbook,rowAccessWindowSize,compressTmpFiles);
    }
}
