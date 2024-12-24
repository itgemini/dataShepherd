/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.service;

import com.datashepherd.annotation.Child;
import com.datashepherd.annotation.ExcelColumn;
import com.datashepherd.exception.ReadException;
import com.datashepherd.helper.Children;
import com.datashepherd.helper.ConditionalMarker;
import com.datashepherd.helper.reader.Processor;
import com.datashepherd.helper.reader.Structure;
import com.datashepherd.helper.writer.style.condional.Registry;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Reader<T> extends ConditionalMarker {
    private final Class<T> entityClass;
    private final List<Structure> structures = new ArrayList<>();
    private final List<Children> subs = new ArrayList<>();
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private static final Function<Cell, Object> TEXT = Cell::getStringCellValue;
    private static final Function<Cell, Object> INTEGER = cell -> (int) cell.getNumericCellValue();
    private static final Function<Cell, Object> DOUBLE = Cell::getNumericCellValue;
    private static final Function<Cell, Object> FLOAT = cell -> (float) cell.getNumericCellValue();
    private static final Function<Cell, Object> LONG = cell -> (long) cell.getNumericCellValue();
    private static final Function<Cell, Object> BOOLEAN = Cell::getBooleanCellValue;
    private static final Function<Cell, Object> DATE = Cell::getDateCellValue;
    private static final Function<Cell, Object> LOCAL_DATE = cell -> cell.getLocalDateTimeCellValue().toLocalDate();
    private static final Function<Cell, Object> LOCAL_DATE_TIME = Cell::getLocalDateTimeCellValue;

    public Reader(Workbook workbook, Class<T> entityClass) {
        super(new Registry(),workbook.getSheet(entityClass.getAnnotation(com.datashepherd.annotation.Sheet.class).name()));
        if(!entityClass.isAnnotationPresent(com.datashepherd.annotation.Sheet.class)) {
            throw new ReadException("Entity class does not have a Sheet annotation");
        }
        this.entityClass = entityClass;
        createStructure();
    }

    private void createStructure() {
        if(Stream.of(entityClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .allMatch(field -> field.getAnnotation(ExcelColumn.class).position()==0)){
            AtomicInteger order = new AtomicInteger(0);
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelColumn.class) || field.isAnnotationPresent(Child.class)) {
                    fieldStructure(order.getAndIncrement(), field);
                }
            }
        }else {
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelColumn.class) || field.isAnnotationPresent(Child.class)) {
                    fieldStructure(field.getAnnotation(ExcelColumn.class).position(), field);
                }
            }
        }
    }

    private void fieldStructure(int order, Field field) {
        switch (field.getType().getName()) {
            case "java.lang.Integer" -> structures.add(new Structure(field.getName(), order, INTEGER,Integer.class));
            case "int" -> structures.add(new Structure(field.getName(), order, INTEGER,int.class));
            case "java.lang.Double" -> structures.add(new Structure(field.getName(), order, DOUBLE,Double.class));
            case "double" -> structures.add(new Structure(field.getName(), order, DOUBLE,double.class));
            case "java.lang.Float" -> structures.add(new Structure(field.getName(), order, FLOAT,Float.class));
            case "float" -> structures.add(new Structure(field.getName(), order, FLOAT,float.class));
            case "java.lang.Long" -> structures.add(new Structure(field.getName(), order, LONG,Long.class));
            case "long" -> structures.add(new Structure(field.getName(), order, LONG,long.class));
            case "java.lang.Boolean" -> structures.add(new Structure(field.getName(), order, BOOLEAN,Boolean.class));
            case "boolean" -> structures.add(new Structure(field.getName(), order, BOOLEAN,boolean.class));
            case "java.util.Date" -> structures.add(new Structure(field.getName(), order, DATE, Date.class));
            case "java.time.LocalDate" -> structures.add(new Structure(field.getName(), order, LOCAL_DATE, LocalDate.class));
            case "java.time.LocalDateTime" -> structures.add(new Structure(field.getName(), order, LOCAL_DATE_TIME, LocalDateTime.class));
            case "java.lang.String" -> structures.add(new Structure(field.getName(), order, TEXT,String.class));
            default -> {
                if(field.isAnnotationPresent(Child.class)) subs.add(new Children(field.getName(),field.getAnnotation(Child.class).mappedBy(),field.getAnnotation(Child.class).referencedBy()));
                else logger.warning("Unsupported data type");
            }
        }
        validationCommentRegistry(field);
        validationStatusRegistry(field);
    }


    public List<T> read() {
        List<T> parents = StreamSupport.stream(sheet.spliterator(), false)
                .skip(1)
                .map(cells -> {
                    try {
                        return readRow(cells);
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                             IllegalAccessException e) {
                        throw new ReadException("Failed to read row ".concat(String.valueOf(cells.getRowNum())), e);
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
        new Processor<>(entityClass,subs,workbook).processChild(parents);
        registry.execute();
        return parents;
    }

    private T readRow(Row row) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T instance = entityClass.getDeclaredConstructor().newInstance();
        for (Structure structure : structures) {
            try {
                Cell cell = row.getCell(structure.order());
                if(Objects.nonNull(cell)) {
                    Object apply = structure.processor().apply(cell);
                    conditional.stream()
                            .filter(conditional -> conditional.name().equals(structure.name()))
                            .forEach(conditional -> conditional.processor().accept(cell, apply));
                    entityClass.getDeclaredMethod("set".concat(StringUtils.capitalize(structure.name())),structure.type())
                            .invoke(instance, apply);
                }
            } catch (Exception e) {
                throw new ReadException("Failed to set value of the failed name ".concat(structure.name()), e);
            }
        }
        return instance;
    }
}