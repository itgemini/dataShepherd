/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.service;

import com.datashepherd.annotation.Cell;
import com.datashepherd.annotation.Image;
import com.datashepherd.annotation.Sheet;
import com.datashepherd.exception.WorkbookException;
import com.datashepherd.exception.WriteException;
import com.datashepherd.helper.WorkbookFactory;
import com.datashepherd.helper.WorkbookType;
import com.datashepherd.helper.writer.model.FormatHandler;
import com.datashepherd.helper.writer.style.ExcelStyleHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.datashepherd.helper.writer.InitiateExcelStructure.*;
import static com.datashepherd.service.Writer.MESSAGE;

public class WriterService extends ExcelService implements ExcelStyleHandler {
    /**
     * Initializes the workbook with a template provided as an InputStream for XLSX format files.
     * This method sets the workbook type to XSSF and creates a new workbook instance based on the provided template InputStream.
     * It encapsulates the complexity of initializing a workbook with a template, making it easier to work with Excel files programmatically.
     * If any errors occur during the workbook creation, a WorkbookException is thrown.
     *
     * @param template The InputStream of the template file for initializing the workbook. Must not be null.
     * @return The current instance of WriterService for method chaining.
     */
    public WriterService xlsx(InputStream template){
        xlsxWork(template);
        return this;
    }

    /**
     * Initializes the workbook with a template file path for XLSX format files.
     * This method sets the workbook type to XSSF and creates a new workbook instance by loading the template from the specified path.
     * It simplifies the process of working with Excel files by abstracting the initialization details.
     * If any errors occur during the workbook creation, a WorkbookException is thrown.
     *
     * @param templatePath The filesystem path to the template file. This file should exist and be accessible.
     * @return The current instance of WriterService for method chaining.
     */
    public WriterService xlsx(String templatePath){
        xlsxWork(templatePath);
        return this;
    }

    /**
     * Initializes the workbook with a template file path for XLS format files.
     * This method sets the workbook type to HSSF and creates a new workbook instance by loading the template from the specified path.
     * It is designed for working with older Excel files and simplifies the initialization process.
     * If any errors occur during the workbook creation, a WorkbookException is thrown.
     *
     * @param templatePath The filesystem path to the template file. This file should exist and be accessible.
     * @return The current instance of WriterService for method chaining.
     */
    public WriterService xls(String templatePath){
        xlsWork(templatePath);
        return this;
    }

    /**
     * Initializes the workbook with a template provided as an InputStream for XLS format files.
     * This method sets the workbook type to HSSF and creates a new workbook instance based on the provided template InputStream.
     * It allows for the manipulation of older Excel files by abstracting the initialization details.
     * If any errors occur during the workbook creation, a WorkbookException is thrown.
     *
     * @param template The InputStream of the template file for initializing the workbook. Must not be null.
     * @return The current instance of WriterService for method chaining.
     */
    public WriterService xls(InputStream template){
        xlsWork(template);
        return this;
    }

    /**
     * Initializes the workbook as an XSSFWorkbook instance, which is suitable for reading and writing Excel files in the newer XLSX format.
     * If this method is not called explicitly, XSSFWorkbook will be used by default.
     *
     * @return The instance with the workbook initialized as XSSFWorkbook.
     * @throws WorkbookException If an error occurs while creating the XSSFWorkbook instance.
     */
    public WriterService xlsx() {
        try {
            type = WorkbookType.XSSF;
            workbook = WorkbookFactory.createWorkbook(type);
        } catch (ReflectiveOperationException e) {
            throw new WorkbookException("Error creating XSSFWorkbook instance",e);
        }
        return this;
    }

    /**
     * Initializes the workbook as an SXSSFWorkbook instance, which is suitable for reading and writing large ExcelService<T> files.
     *
     * @return The WriterService instance with the workbook initialized as SXSSFWorkbook.
     * @throws WorkbookException If an error occurs while creating the SXSSFWorkbook instance.
     */
    public WriterService xlsxLarge() {
        try {
            if(workbook == null) {
                throw new WorkbookException("XSSFWorkbook is not set, please call xlsx() or xlsx(InputStream template) first");
            }
            type = WorkbookType.SXSSF;
            workbook = WorkbookFactory.createWorkbook((XSSFWorkbook) workbook);
        } catch (ReflectiveOperationException e) {
            throw new WorkbookException("Error creating SXSSFWorkbook instance",e);
        }
        return this;
    }

    /**
     * Initializes the workbook as an SXSSFWorkbook instance, which is suitable for reading and writing large ExcelService<T> files.
     *
     * @param rowAccessWindowSize The number of rows to keep in memory while streaming.
     * @param compressTmpFiles Whether temporary files should be compressed.
     * @return The WriterService instance with the workbook initialized as SXSSFWorkbook.
     * @throws WorkbookException If an error occurs while creating the SXSSFWorkbook instance.
     */
    public WriterService xlsxLargeWithTemplateAndRowAccessWindowSize(int rowAccessWindowSize, boolean compressTmpFiles) {
        try {
            type = WorkbookType.SXSSF;
            workbook = WorkbookFactory.createWorkbook((XSSFWorkbook) workbook,rowAccessWindowSize,compressTmpFiles);
        } catch (ReflectiveOperationException e) {
            throw new WorkbookException("Error creating SXSSFWorkbook instance, (NB) this function should called after xlsx function with parameter template",e);
        }
        return this;
    }

    /**
     * Initializes the workbook as an HSSFWorkbook instance, which is suitable for reading and writing ExcelService<T> files in the older XLS format.
     *
     * @return The WriterService instance with the workbook initialized as HSSFWorkbook.
     * @throws WorkbookException If an error occurs while creating the HSSFWorkbook instance.
     */
    public WriterService xls() {
        try {
            type = WorkbookType.HSSF;
            workbook = WorkbookFactory.createWorkbook(WorkbookType.HSSF);
        } catch (ReflectiveOperationException e) {
            throw new WorkbookException("Error creating HSSFWorkbook instance",e);
        }
        return this;
    }

    /**
     * Sets up a template for the Excel workbook based on the annotations present in the provided template object.
     * This method requires the template object to be annotated with {@link Sheet} at the class level to define the sheet name.
     * It iterates through all the fields of the template object, looking for fields annotated with {@link Cell},
     * to map them to the corresponding cells in the Excel sheet.
     * The method performs the following steps:
     * 1. Checks if the workbook has been initialized; if not, throws a {@link WorkbookException}.
     * 2. Retrieves the {@link Sheet} annotation from the template object to determine the sheet name.
     *    If the {@link Sheet} annotation is not present, throws an {@link UnsupportedOperationException}.
     * 3. Determines the sheet name. If the name attribute in the {@link Sheet} annotation is blank,
     *    the class name of the template object is used as the sheet name.
     * 4. Checks if a sheet with the determined name already exists in the workbook; if not, creates a new sheet.
     * 5. Iterates through all declared fields of the template object, looking for fields annotated with {@link Cell}.
     *    For each annotated field, it updates the corresponding cell in the sheet with the field's value.
     *
     * @param <T> The type of the template object.
     * @param template The template object annotated with {@link Sheet} and {@link Cell} annotations.
     * @return The current instance of {@link WriterService} for method chaining.
     * @throws WorkbookException If the workbook has not been initialized before calling this method.
     * @throws UnsupportedOperationException If the template object does not have a {@link Sheet} annotation.
     * @throws WriteException If an error occurs while accessing the fields of the template object.
     */
    public <T> WriterService cover(T template) {
        if(workbook == null) {
            throw new WorkbookException("Workbook is not set");
        }
        Sheet sheetAttributes = template.getClass().getAnnotation(Sheet.class);
        if (Objects.isNull(sheetAttributes)) throw new UnsupportedOperationException("unsupported class that not use the Sheet annotation");
        String name = StringUtils.isBlank(sheetAttributes.name()) ? template.getClass().getSimpleName() : sheetAttributes.name();
        org.apache.poi.ss.usermodel.Sheet sheet = Objects.nonNull(workbook.getSheet(sheetAttributes.name())) ? workbook.getSheet(name) : workbook.createSheet(name);
        for (Field field : template.getClass().getDeclaredFields()) {
            try {
               if(!(workbook instanceof SXSSFWorkbook)) updateTemplate(field, template, sheet);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new WriteException(MESSAGE, e);
            }
        }
        return this;
    }

    private <T> void updateTemplate(Field field, T template, org.apache.poi.ss.usermodel.Sheet sheet) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(field.isAnnotationPresent(Cell.class)) {
            var clazz = template.getClass();
            Cell cell = field.getAnnotation(Cell.class);
            var value = clazz.getDeclaredMethod("get".concat(StringUtils.capitalize(field.getName()))).invoke(template);
            if(Math.addExact(Math.addExact(cell.firstRow(), cell.lastRow()),Math.addExact(cell.firstColumn(), cell.lastColumn()))==0){
                Row row = Optional.ofNullable(sheet.getRow(cell.row())).orElseGet(()->sheet.createRow(cell.row()));
                org.apache.poi.ss.usermodel.Cell cellC = Optional.ofNullable(row.getCell(cell.column())).orElseGet(()->row.createCell(cell.column()));
                setInfo(cellC, field, value);
                style(FormatHandler.getInstance(workbook),workbook,field,cellC,cell.format());
            } else {
                Row row = Optional.ofNullable(sheet.getRow(cell.firstRow())).orElseGet(()->sheet.createRow(cell.firstRow()));
                Optional.ofNullable(sheet.getRow(cell.lastRow())).orElseGet(()->sheet.createRow(cell.lastRow()));
                org.apache.poi.ss.usermodel.Cell cellC = Optional.ofNullable(row.getCell(cell.firstColumn())).orElseGet(()->row.createCell(cell.firstColumn()));
                Optional.ofNullable(row.getCell(cell.lastColumn())).orElseGet(()->row.createCell(cell.lastColumn()));
                CellRangeAddress cellAddresses = new CellRangeAddress(cell.firstRow(),cell.lastRow(),cell.firstColumn(),cell.lastColumn());
                sheet.addMergedRegionUnsafe(cellAddresses);
                setInfo(cellC, field, value);
                style(FormatHandler.getInstance(workbook),workbook,field,cellC,cell.format());
            }
        }
    }

    private <T> void setInfo(org.apache.poi.ss.usermodel.Cell cellC, Field field, Object value) {
        switch (field.getType().getName()){
            case "java.lang.Integer", "int" -> INTEGER.accept(cellC, value);
            case "java.lang.Double", "double" -> DOUBLE.accept(cellC, value);
            case "java.lang.Float", "float" -> FLOAT.accept(cellC, value);
            case "java.lang.Long", "long" -> LONG.accept(cellC, value);
            case "java.lang.Boolean", "boolean" -> BOOLEAN.accept(cellC, value);
            case "java.lang.Date", "date" -> DATE.accept(cellC, value);
            case "java.time.LocalDate" -> LOCAL_DATE.accept(cellC, value);
            case "java.time.LocalDateTime" -> LOCAL_DATE_TIME.accept(cellC, value);
            case "java.lang.String" -> TEXT.accept(cellC, value);
            case "java.lang.Byte[]", "byte[]", "[B" ->
                    IMAGE.accept(cellC, field.isAnnotationPresent(Image.class) ? Pair.of(field.getAnnotation(Image.class), value) : value);
            default -> throw new IllegalStateException("Unexpected value: " + field.getType().getName());
        }
    }

    /**
     * Writes data to the Excel workbook.
     * This method takes a list of data of any type T and writes it to the workbook using a generic Writer class.
     * The method ensures that a workbook has been initialized before attempting to write data. If the workbook
     * is not set, it throws a WorkbookException. This method is designed to be flexible and work with any type
     * of data, provided that the data type T is specified. It leverages the Writer class to handle the specifics
     * of writing data to the workbook.
     *
     * @param <T> The type of the data to be written to the Excel workbook.
     * @param data A list of data items of type T to be written to the workbook.
     * @param entityClass The class of the data type T, used for reflection in the Writer class.
     * @return The current instance of WriterService, allowing for method chaining.
     * @throws WorkbookException If the workbook has not been set prior to calling this method.
     */
    public <T> WriterService writeToExcel(List<T> data, Class<T> entityClass) {
        if(workbook == null) {
            throw new WorkbookException("Workbook is not set");
        }
        new Writer<>(data, entityClass, workbook).write();
        return this;
    }
}
