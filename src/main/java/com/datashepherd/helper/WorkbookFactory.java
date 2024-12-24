/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper;


import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

import static com.datashepherd.helper.WorkbookType.SXSSF;

public class WorkbookFactory {
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
