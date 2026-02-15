/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import com.datashepherd.excel.annotation.Child;
import com.datashepherd.excel.annotation.ExcelColumn;
import com.datashepherd.excel.annotation.Sheet;
import com.datashepherd.excel.exception.ReadENDException;
import com.datashepherd.excel.exception.ReadException;
import com.datashepherd.excel.helper.Children;
import com.datashepherd.excel.helper.ConditionalMarker;
import com.datashepherd.excel.helper.ExcelMetadataReader;
import com.datashepherd.excel.helper.reader.Processor;
import com.datashepherd.excel.helper.reader.Structure;
import com.datashepherd.excel.helper.reader.TypeExtractorRegistry;
import com.datashepherd.excel.helper.writer.model.WritingContext;
import com.datashepherd.excel.helper.writer.style.condional.Registry;

import static org.apache.poi.ss.usermodel.CellType.STRING;

public class Reader<T> extends ConditionalMarker {
    private final Class<T> entityClass;
    private final String endSheet;
    private final Integer skipHeader;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final ConcurrentLinkedQueue<Structure> structures = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Children> subs = new ConcurrentLinkedQueue<>();

    /**
     * Initializes a new Reader instance for a specific entity class.
     *
     * @param workbook    the workbook to read from.
     * @param entityClass the class of the objects to be read.
     */
    public Reader(Workbook workbook, Class<T> entityClass) {
        super(WritingContext.of(workbook, workbook.getSheet(ExcelMetadataReader.getSheetAnnotation(entityClass)
                                                                               .name()), new Registry()));
        Sheet sheetAnn = ExcelMetadataReader.getSheetAnnotation(entityClass);
        this.endSheet = sheetAnn.endSheet();
        this.skipHeader = sheetAnn.skipHeader();
        this.entityClass = entityClass;
        checkEndSheet();
        createStructure();
    }

    /**
     * Creates the internal reading structure based on entity annotations.
     */
    private void createStructure() {
        if (Objects.isNull(context.sheet())) return;
        if (ExcelMetadataReader.hasAllDefaultPositions(entityClass)) {
            AtomicInteger order = new AtomicInteger(0);
            Stream.of(entityClass.getDeclaredFields())
                  .filter(field -> field.isAnnotationPresent(ExcelColumn.class) || field.isAnnotationPresent(Child.class))
                  .forEachOrdered(field -> fieldStructure(order.getAndIncrement(), field));
        }
        else {
            Stream.of(entityClass.getDeclaredFields())
                  .filter(field -> field.isAnnotationPresent(ExcelColumn.class) || field.isAnnotationPresent(Child.class))
                  .forEachOrdered(field -> {
                      int position = field.isAnnotationPresent(ExcelColumn.class)
                              ? field.getAnnotation(ExcelColumn.class).position()
                              : 0; // Default or handled by fieldStructure
                      fieldStructure(position, field);
                  });
        }
    }

    /**
     * Defines the structure for a specific field and registers it for reading.
     *
     * @param order the column position.
     * @param field the Java field.
     */
    private void fieldStructure(int order, Field field) {
        Function<Cell, Optional<Object>> extractor = TypeExtractorRegistry.getExtractor(field.getType().getName());
        if (extractor != null) {
            structures.add(new Structure(field.getName(), order, extractor, field.getType()));
        }
        else if (field.isAnnotationPresent(Child.class)) {
            Child childAnn = field.getAnnotation(Child.class);
            subs.add(new Children(field.getName(), childAnn.mappedBy(), childAnn.referencedBy()));
        }
        else {
            logger.warning("Unsupported data type: " + field.getType().getName());
        }
        registerValidationComment(field);
        registerValidationStatus(field);
    }

    /**
     * Reads all data from the sheet into a collection of objects.
     *
     * @return a concurrent queue of objects containing the data.
     */
    public ConcurrentLinkedQueue<T> read() {
        if (Objects.isNull(context.sheet())) return new ConcurrentLinkedQueue<>();
        int headerRowNum = ExcelMetadataReader.getSheetAnnotation(entityClass).headerRow();
        ConcurrentLinkedQueue<T> parents = StreamSupport.stream(context.sheet().spliterator(), false)
                .takeWhile(row -> row.cellIterator().hasNext()
                        && !(StringUtils.isNoneBlank(endSheet) && row.cellIterator().next().getCellType().equals(STRING)
                        && row.cellIterator().next().getStringCellValue().equals(endSheet)))
                                                        .skip(Math.max(skipHeader, headerRowNum + 1))
                .map(cells -> {
                    try {
                        return readRow(cells);
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                             IllegalAccessException e) {
                        throw new ReadException("Failed to read row ".concat(String.valueOf(cells.getRowNum())), e);
                    }
                }).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
        new Processor<>(entityClass, subs, context.workbook()).processChild(parents);
        context.registry().execute();
        return parents;
    }

    /**
     * Checks if the end of the sheet marker is present if required.
     */
    private void checkEndSheet() {
        if (Objects.isNull(context.sheet()) || StringUtils.isBlank(endSheet)) return;
        StreamSupport.stream(context.sheet().spliterator(), false)
                .filter(row -> row.cellIterator().hasNext()
                        && (StringUtils.isNoneBlank(endSheet) && row.cellIterator().next().getCellType().equals(STRING)
                        && row.cellIterator().next().getStringCellValue().equals(endSheet)))
                .findAny().orElseThrow(() -> new ReadENDException(String.format("The %s is missing", endSheet)));
    }

    /**
     * Reads a single row and maps it to an object instance.
     *
     * @param row the Excel row.
     * @return the object instance.
     */
    private T readRow(Row row) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T instance = entityClass.getDeclaredConstructor().newInstance();
        for (Structure structure : structures) {
            Cell cell = row.getCell(structure.order());
            if (Objects.nonNull(cell)) {
                Optional<Object> apply = structure.processor().apply(cell);
                Object val = apply.orElse(null);
                conditional.stream()
                        .filter(conditional -> conditional.name().equals(structure.name()))
                           .forEach(conditional -> conditional.processor().accept(cell, val));
                apply.ifPresent(value -> {
                    try {
                        entityClass.getDeclaredMethod("set".concat(StringUtils.capitalize(structure.name())), structure.type())
                                .invoke(instance, value);
                    } catch (Exception e) {
                        throw new ReadException(String.format("Failed to set value of the failed name %s from sheet name %s and line number %s, please check your class name %s", structure.name(), context.sheet()
                                                                                                                                                                                                           .getSheetName(), row.getRowNum(), entityClass.getSimpleName()), e);
                    }
                });
            }
        }
        return instance;
    }
}