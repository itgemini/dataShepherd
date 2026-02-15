/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import com.datashepherd.excel.annotation.ExcelColumn;
import com.datashepherd.excel.annotation.Image;
import com.datashepherd.excel.annotation.Sheet;
import com.datashepherd.excel.annotation.style.ExcelStyle;
import com.datashepherd.excel.exception.WorkbookException;
import com.datashepherd.excel.helper.ExcelMetadataReader;
import com.datashepherd.excel.helper.writer.Elements;
import com.datashepherd.excel.helper.writer.InitiateExcelStructure;
import com.datashepherd.excel.helper.writer.model.SheetPictureHandler;
import com.datashepherd.excel.helper.writer.model.WritingContext;
import com.datashepherd.excel.helper.writer.style.condional.Registry;

/**
 * Writer class extends Excel class and is responsible for writing data to an Excel sheet.
 * It uses Apache POI library to interact with Excel files.
 *
 * @param <T> the type of objects that this writer will write to the Excel sheet.
 */
public class Writer<T> {
    private final Workbook workbook;
    private final Elements elements;
    private final org.apache.poi.ss.usermodel.Sheet sheet;
    private final List<T> sources;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    public static final String MESSAGE = "Failed write the value of the field : {0}";
    private final Registry registry;
    private final Map<Class<Object>,List<Object>> writers = new ConcurrentHashMap<>();
    private final WritingContext context;

    /**
     * Constructor for the Writer class.
     *
     * @param sources      the list of objects to be written to the Excel sheet.
     * @param entityClass  the class of the objects to be written to the Excel sheet.
     */
    public Writer(List<T> sources, Class<T> entityClass, Workbook workbook) {
        this.registry = new Registry();
        this.sources = Collections.synchronizedList(sources);
        this.workbook = workbook;
        Sheet sheetAttributes = ExcelMetadataReader.getSheetAnnotation(entityClass);
        this.sheet = sheet(StringUtils.isBlank(sheetAttributes.name()) ? entityClass.getSimpleName() : sheetAttributes.name());
        this.context = WritingContext.of(workbook, sheet, registry);
        this.elements = header(entityClass);
    }

    /**
     * Writes the data from the sources to the Excel sheet.
     */
    /**
     * Internal write method to process rows and handle master-detail relationships.
     */
    protected void write() {
        sources.forEach(source -> writeLine(source, sheet.createRow(sheet.getLastRowNum() == -1 ? elements.headerRow() + 1 : sheet.getLastRowNum() + 1)));
        registry.execute();
        registry.clear();
        sources.clear();
        writers.forEach((objectClass, objects) -> {
            Writer<Object> writer = new Writer<>(objects, objectClass, workbook);
            writer.write();
            writer.getWriters().clear();
        });
        writers.clear();
    }

    /**
     * Returns the internal map of writers for child entities.
     *
     * @return the map of writers.
     */
    protected Map<Class<Object>,List<Object>> getWriters(){return writers;}

    /**
     * Returns the sheet with the given name. If the sheet does not exist, a new one is created.
     *
     * @param name  the name of the sheet.
     * @return      the sheet with the given name.
     */
    public org.apache.poi.ss.usermodel.Sheet sheet(String name) {
        if(Objects.isNull(workbook)) throw new WorkbookException("Workbook is not initialized");
        if (Objects.nonNull(workbook.getSheet(name))) return workbook.getSheet(name);
        else return workbook.createSheet(name);
    }

    /**
     * Sets the header of the sheet at the given position.
     *
     * @param value     the value of the header.
     * @param position  the position of the header.
     * @param sheet     the sheet where the header is to be set.
     */
    private void sheetHeader(String value, Position position, org.apache.poi.ss.usermodel.Sheet sheet) {
        if (StringUtils.isNotBlank(value)) {
            switch (position) {
                case CENTER -> sheet.getHeader().setCenter(value);
                case LEFT -> sheet.getHeader().setLeft(value);
                case RIGHT -> sheet.getHeader().setRight(value);
            }
        }
    }

    /**
     * Sets the footer of the sheet at the given position.
     *
     * @param value     the value of the footer.
     * @param position  the position of the footer.
     * @param sheet     the sheet where the footer is to be set.
     */
    private void sheetFooter(String value, Position position, org.apache.poi.ss.usermodel.Sheet sheet) {
        if (StringUtils.isNotBlank(value)) {
            switch (position) {
                case CENTER -> sheet.getFooter().setCenter(value);
                case LEFT -> sheet.getFooter().setLeft(value);
                case RIGHT -> sheet.getFooter().setRight(value);
            }
        }
    }

    /**
     * Enum for the positions of the header and footer.
     */
    private enum Position {
        CENTER,
        LEFT,
        RIGHT
    }

    /**
     * Initializes the sheet, sets headers and footers, and sets up the Excel structure.
     *
     * @param entityClass  the class of the objects to be written to the Excel sheet.
     * @return             the elements of the header of the sheet.
     */
    private Elements header(Class<T> entityClass) {
        Sheet sheetAttributes = ExcelMetadataReader.getSheetAnnotation(entityClass);
        if(!(workbook instanceof SXSSFWorkbook)) {
            sheetHeader(sheetAttributes.centerHeader(), Position.CENTER, sheet);
            sheetHeader(sheetAttributes.leftHeader(), Position.LEFT, sheet);
            sheetHeader(sheetAttributes.rightHeader(), Position.RIGHT, sheet);

            sheetFooter(sheetAttributes.centerFooter(), Position.CENTER, sheet);
            sheetFooter(sheetAttributes.leftFooter(), Position.LEFT, sheet);
            sheetFooter(sheetAttributes.rightFooter(), Position.RIGHT, sheet);
            new SheetPictureHandler(sheet, entityClass);
            registry.onComplete(()-> elements.structures().forEach(structure -> {
                sheet.autoSizeColumn(structure.order());
                sheet.setDefaultColumnWidth(structure.order());
            }));
        }
        return new InitiateExcelStructure(context, entityClass).getElements();
    }

    /**
     * Writes a single object as a row to the Excel sheet and processes child entities.
     * @param o         the object to be written.
     * @param cells     the row where the data will be written.
     */
    protected void writeLine(Object o, org.apache.poi.ss.usermodel.Row cells) {
        processElements(o,cells);
        processChild(o);
    }

    /**
     * Processes the fields of an object and writes them to the specified row cells.
     *
     * @param o     the object to process.
     * @param cells the Excel row.
     */
    private void processElements(Object o, org.apache.poi.ss.usermodel.Row cells) {
        var clazz = o.getClass();
        for (var structure : elements.structures()) {
            String name = structure.name();
            Integer order = structure.order();
            var processor = structure.processor();
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                var value = field.get(o);
                var cell = cells.createCell(order);
                processor.accept(cell, field.isAnnotationPresent(Image.class) ? Pair.of(field.getAnnotation(Image.class), value) : value);
                elements.conditional().stream()
                        .filter(conditional -> conditional.name().equals(name))
                        .forEach(conditional -> conditional.processor().accept(cell, value));
                ExcelColumn column = field.getAnnotation(ExcelColumn.class);
                if (Objects.nonNull(column) && (StringUtils.isNotBlank(column.format()) || field.isAnnotationPresent(ExcelStyle.class))) {
                    registry.onBefore(() -> {
                        CellStyle style = context.styleManager()
                                                 .getOrCreateStyle(field.getAnnotation(ExcelStyle.class), column.format());
                        cell.setCellStyle(style);
                    });
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.log(Level.WARNING, MESSAGE, name);
            }
        }
    }

    /**
     * Processes child entities for master-detail relationships.
     *
     * @param o the parent object.
     */
    @SuppressWarnings("unchecked")
    private void processChild(Object o){
        var clazz = o.getClass();
        elements.children().forEach(child -> {
            try {
                Field field = clazz.getDeclaredField(child.name());
                field.setAccessible(true);
                var value = field.get(o);
                if (Object.class.isAssignableFrom(clazz) && value instanceof Collection<?> list) {
                    List<Object> sub = writers.getOrDefault(child.mappedBy(), new ArrayList<>());
                    sub.addAll(list.stream()
                            .map(Object.class::cast)
                            .collect(Collectors.toCollection(ArrayList::new)));
                    writers.put((Class<Object>) child.mappedBy(),sub);
                } else if (Object.class.isAssignableFrom(clazz) && Objects.nonNull(value)) {
                    List<Object> sub = writers.getOrDefault(child.mappedBy(), new ArrayList<>());
                    sub.add(value);
                    writers.put((Class<Object>) child.mappedBy(),sub);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.log(Level.WARNING, MESSAGE, child.name());
            }
        });
    }
}
