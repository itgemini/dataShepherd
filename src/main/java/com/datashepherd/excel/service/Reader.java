/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.service;

import com.datashepherd.excel.annotation.Child;
import com.datashepherd.excel.annotation.ExcelColumn;
import com.datashepherd.excel.annotation.Sheet;
import com.datashepherd.excel.exception.ReadException;
import com.datashepherd.excel.helper.Children;
import com.datashepherd.excel.helper.ConditionalMarker;
import com.datashepherd.excel.helper.reader.Processor;
import com.datashepherd.excel.helper.reader.Structure;
import com.datashepherd.excel.helper.writer.style.condional.Registry;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class Reader<T> extends ConditionalMarker {
    private final Class<T> entityClass;
    private static final Function<Cell, Optional<Object>> TEXT = cell -> {
        Object value = getValue(cell);
        if (Objects.nonNull(value)) return Optional.of(String.valueOf(value));
        return Optional.empty();
    };
    private static final Function<Cell, Optional<Object>> INTEGER = cell -> {
        Object value = getValue(cell);
        if (Objects.nonNull(value) && value instanceof Number object) return Optional.of(object.intValue());
        if (Objects.nonNull(value) && value instanceof String object) return Optional.of(Integer.valueOf(object));
        return Optional.empty();
    };
    private final String endSheet;
    private final Integer skipHeader;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private static final Function<Cell, Optional<Object>> DOUBLE = cell -> {
        Object value = getValue(cell);
        if (Objects.nonNull(value) && value instanceof Number object) return Optional.of(object.doubleValue());
        if (Objects.nonNull(value) && value instanceof String object) return Optional.of(Double.valueOf(object));
        return Optional.empty();
    };
    private static final Function<Cell, Optional<Object>> FLOAT = cell -> {
        Object value = getValue(cell);
        if (Objects.nonNull(value) && value instanceof Number object) return Optional.of(object.floatValue());
        if (Objects.nonNull(value) && value instanceof String object) return Optional.of(Float.valueOf(object));
        return Optional.empty();
    };
    private static final Function<Cell, Optional<Object>> LONG = cell -> {
        Object value = getValue(cell);
        if (Objects.nonNull(value) && value instanceof Number object) return Optional.of(object.longValue());
        if (Objects.nonNull(value) && value instanceof String object) return Optional.of(Long.valueOf(object));
        return Optional.empty();
    };
    private static final Function<Cell, Optional<Object>> BOOLEAN = cell -> {
        Object value = getValue(cell);
        if (Objects.nonNull(value) && value instanceof Boolean object) return Optional.of(object);
        if (Objects.nonNull(value) && value instanceof String object) return Optional.of(Boolean.valueOf(object));
        return Optional.empty();
    };
    private static final Function<Cell, Optional<Object>> DATE = cell -> {
        Object value = getValue(cell);
        if (Objects.nonNull(value) && value instanceof Number) return Optional.of(cell.getDateCellValue());
        if (Objects.nonNull(value) && value instanceof String object)
            return Optional.of(Date.from(Instant.parse(object)));
        return Optional.empty();
    };
    private static final Function<Cell, Optional<Object>> LOCAL_DATE = cell -> {
        Object value = getValue(cell);
        if (Objects.nonNull(value) && value instanceof Number)
            return Optional.of(cell.getLocalDateTimeCellValue().toLocalDate());
        if (Objects.nonNull(value) && value instanceof String object) return Optional.of(LocalDate.parse(object));
        return Optional.empty();
    };
    private static final Function<Cell, Optional<Object>> LOCAL_DATE_TIME = cell -> {
        Object value = getValue(cell);
        if (Objects.nonNull(value) && value instanceof Number) return Optional.of(cell.getLocalDateTimeCellValue());
        if (Objects.nonNull(value) && value instanceof String object) return Optional.of(LocalDateTime.parse(object));
        return Optional.empty();
    };
    private static final String INTEGER_TYPE = "java.lang.Integer";
    private static final String INT_TYPE = "int";
    private static final String DOUBLE_WRAPPER_TYPE = "java.lang.Double";
    private static final String DOUBLE_TYPE = "double";
    private static final String FLOUT_WRAPPER_TYPE = "java.lang.Float";
    private static final String FLOUT_TYPE = "float";
    private static final String LONG_WRAPPER_TYPE = "java.lang.Long";
    private static final String LONG_TYPE = "long";
    private static final String BOOLEAN_WRAPPER_TYPE = "java.lang.Boolean";
    private static final String BOOLEAN_TYPE = "boolean";
    private static final String DATE_TYPE = "java.util.Date";
    private static final String LOCAL_DATE_TYPE = "java.time.LocalDate";
    private static final String LOCAL_DATE_TIME_TYPE = "java.time.LocalDateTime";
    private static final String STRING_TYPE = "java.lang.String";
    private final ConcurrentLinkedQueue<Structure> structures = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Children> subs = new ConcurrentLinkedQueue<>();

    public Reader(Workbook workbook, Class<T> entityClass) {
        super(new Registry(), workbook, workbook.getSheet(Objects.requireNonNull(entityClass.getAnnotation(Sheet.class)).name()));
        if (!entityClass.isAnnotationPresent(Sheet.class)) {
            throw new ReadException("Entity class does not have a Sheet annotation");
        }
        this.endSheet = Objects.requireNonNull(entityClass.getAnnotation(Sheet.class)).endSheet();
        this.skipHeader = Objects.requireNonNull(entityClass.getAnnotation(Sheet.class)).skipHeader();
        this.entityClass = entityClass;
        createStructure();
    }

    private void createStructure() {
        if (Objects.isNull(sheet)) return;
        if (Stream.of(entityClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .allMatch(field -> Objects.requireNonNull(field.getAnnotation(ExcelColumn.class)).position() == 0)) {
            AtomicInteger order = new AtomicInteger(0);
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelColumn.class) || field.isAnnotationPresent(Child.class)) {
                    fieldStructure(order.getAndIncrement(), field);
                }
            }
        } else {
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelColumn.class) || field.isAnnotationPresent(Child.class)) {
                    fieldStructure(Objects.requireNonNull(field.getAnnotation(ExcelColumn.class)).position(), field);
                }
            }
        }
    }

    private static Object getValue(Cell cell) {
        if (NUMERIC.equals(cell.getCellType())) {
            return cell.getNumericCellValue();
        } else if (STRING.equals(cell.getCellType())) {
            return cell.getStringCellValue();
        } else if (CellType.BOOLEAN.equals(cell.getCellType())) {
            return cell.getBooleanCellValue();
        } else {
            return null;
        }
    }

    private void fieldStructure(int order, Field field) {
        switch (field.getType().getName()) {
            case INTEGER_TYPE -> structures.add(new Structure(field.getName(), order, INTEGER, Integer.class));
            case INT_TYPE -> structures.add(new Structure(field.getName(), order, INTEGER, int.class));
            case DOUBLE_WRAPPER_TYPE -> structures.add(new Structure(field.getName(), order, DOUBLE, Double.class));
            case DOUBLE_TYPE -> structures.add(new Structure(field.getName(), order, DOUBLE, double.class));
            case FLOUT_WRAPPER_TYPE -> structures.add(new Structure(field.getName(), order, FLOAT, Float.class));
            case FLOUT_TYPE -> structures.add(new Structure(field.getName(), order, FLOAT, float.class));
            case LONG_WRAPPER_TYPE -> structures.add(new Structure(field.getName(), order, Reader.LONG, Long.class));
            case LONG_TYPE -> structures.add(new Structure(field.getName(), order, Reader.LONG, long.class));
            case BOOLEAN_WRAPPER_TYPE ->
                    structures.add(new Structure(field.getName(), order, Reader.BOOLEAN, Boolean.class));
            case BOOLEAN_TYPE -> structures.add(new Structure(field.getName(), order, Reader.BOOLEAN, boolean.class));
            case DATE_TYPE -> structures.add(new Structure(field.getName(), order, DATE, Date.class));
            case LOCAL_DATE_TYPE -> structures.add(new Structure(field.getName(), order, LOCAL_DATE, LocalDate.class));
            case LOCAL_DATE_TIME_TYPE ->
                    structures.add(new Structure(field.getName(), order, LOCAL_DATE_TIME, LocalDateTime.class));
            case STRING_TYPE -> structures.add(new Structure(field.getName(), order, TEXT, String.class));
            default -> {
                if (field.isAnnotationPresent(Child.class))
                    subs.add(new Children(field.getName(), Objects.requireNonNull(field.getAnnotation(Child.class)).mappedBy(), Objects.requireNonNull(field.getAnnotation(Child.class)).referencedBy()));
                else logger.warning("Unsupported data type");
            }
        }
        validationCommentRegistry(field);
        validationStatusRegistry(field);
    }

    public ConcurrentLinkedQueue<T> read() {
        if (Objects.isNull(sheet)) return new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<T> parents = StreamSupport.stream(sheet.spliterator(), false)
                .takeWhile(row -> row.cellIterator().hasNext()
                        && !(StringUtils.isNoneBlank(endSheet) && row.cellIterator().next().getCellType().equals(STRING)
                        && row.cellIterator().next().getStringCellValue().equals(endSheet)))
                .skip(skipHeader)
                .map(cells -> {
                    try {
                        return readRow(cells);
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                             IllegalAccessException e) {
                        throw new ReadException("Failed to read row ".concat(String.valueOf(cells.getRowNum())), e);
                    }
                }).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
        new Processor<>(entityClass, subs, workbook).processChild(parents);
        registry.execute();
        return parents;
    }

    private T readRow(Row row) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T instance = entityClass.getDeclaredConstructor().newInstance();
        for (Structure structure : structures) {
            Cell cell = row.getCell(structure.order());
            if (Objects.nonNull(cell)) {
                Optional<Object> apply = structure.processor().apply(cell);
                conditional.stream()
                        .filter(conditional -> conditional.name().equals(structure.name()))
                        .forEach(conditional -> conditional.processor().accept(cell, apply));
                apply.ifPresent(value -> {
                    try {
                        entityClass.getDeclaredMethod("set".concat(StringUtils.capitalize(structure.name())), structure.type())
                                .invoke(instance, value);
                    } catch (Exception e) {
                        throw new ReadException(String.format("Failed to set value of the failed name %s from sheet name %s and line number %s, please check your class name %s", structure.name(), sheet.getSheetName(), row.getRowNum(), entityClass.getSimpleName()), e);
                    }
                });
            }
        }
        return instance;
    }
}