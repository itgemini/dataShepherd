/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.service;

import com.datashepherd.exception.WorkbookException;

import java.io.InputStream;
import java.util.List;

public class ReaderService extends ExcelService {
    /**
     * Prepares the workbook for reading from an XLSX format file.
     * This method sets the workbook type to XSSF and initializes a new workbook instance using the provided InputStream.
     * It simplifies the process of setting up the workbook for reading data from an Excel file.
     * If any errors occur during the workbook initialization, a WorkbookException is thrown.
     *
     * @param path The InputStream of the XLSX file to initialize the workbook. Must not be null.
     * @return The current instance of ReaderService for method chaining.
     */
    public ReaderService xlsx(InputStream path) {
        xlsxWork(path);
        return this;
    }

    /**
     * Prepares the workbook for reading from an XLSX format file.
     * This method sets the workbook type to XSSF and initializes a new workbook instance using the provided InputStream.
     * It simplifies the process of setting up the workbook for reading data from an Excel file.
     * If any errors occur during the workbook initialization, a WorkbookException is thrown.
     *
     * @param path The InputStream of the XLSX file to initialize the workbook. Must not be null.
     * @return The current instance of ReaderService for method chaining.
     */
    public ReaderService xlsx(String path){
        xlsxWork(path);
        return this;
    }

    /**
     * Prepares the workbook for reading from an XLS format file.
     * This method sets the workbook type to HSSF and initializes a new workbook instance using the provided InputStream.
     * It simplifies the process of setting up the workbook for reading data from an Excel file.
     * If any errors occur during the workbook initialization, a WorkbookException is thrown.
     *
     * @param path The filesystem path to the template file. This file should exist and be accessible.
     * @return The current instance of ReaderService for method chaining.
     */
    public ReaderService xls(String path){
        xlsWork(path);
        return this;
    }

    /**
     * Prepares the workbook for reading from an XLS format file.
     * This method sets the workbook type to HSSF and initializes a new workbook instance using the provided InputStream.
     * It simplifies the process of setting up the workbook for reading data from an Excel file.
     * If any errors occur during the workbook initialization, a WorkbookException is thrown.
     *
     * @param path The InputStream of the template file. This file should exist and be accessible.
     * @return The current instance of ReaderService for method chaining.
     */
    public ReaderService xls(InputStream path){
        xlsWork(path);
        return this;
    }

    /**
     * Reads data from the Excel workbook into a list of type T.
     * This method leverages a generic Reader class to read data from the initialized workbook and return it as a list of objects of type T.
     * It requires that the workbook has been previously initialized and set. If the workbook is not set, a WorkbookException is thrown.
     * This method is designed to be flexible and can work with any type of data, provided that the data type T is specified and a corresponding
     * Reader class is available to handle the conversion from Excel rows to Java objects.
     *
     * @param <T> The type of the data to be read from the Excel workbook.
     * @param entityClass The class of the data type T, used for reflection in the Reader class to instantiate objects of type T.
     * @return A list of objects of type T read from the Excel workbook.
     * @throws WorkbookException If the workbook has not been set prior to calling this method.
     */
    public <T> List<T> readFromExcel(Class<T> entityClass) {
        if(workbook == null) {
            throw new WorkbookException("Workbook is not set");
        }
        Reader<T> reader = new Reader<>(workbook, entityClass);
        return reader.read();
    }
}
