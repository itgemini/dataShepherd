/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer;

import com.datashepherd.annotation.Child;
import com.datashepherd.annotation.ExcelColumn;
import com.datashepherd.annotation.Image;
import com.datashepherd.annotation.style.ConditionalExcelCellStyle;
import com.datashepherd.exception.StyleException;
import com.datashepherd.helper.Children;
import com.datashepherd.helper.ConditionalMarker;
import com.datashepherd.helper.writer.model.CellImageHandler;
import com.datashepherd.helper.writer.style.condional.BackgroundColorCondition;
import com.datashepherd.helper.writer.style.condional.ColorCondition;
import com.datashepherd.helper.writer.style.condional.Registry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class InitiateExcelStructure extends ConditionalMarker {
    private final List<Structure> structures = new ArrayList<>();
    private final List<Children> children = new ArrayList<>();
    private final Elements elements;
    public static final BiConsumer<Cell,Object> TEXT = (cell,value) -> cell.setCellValue((String) value);

    public static final BiConsumer<Cell,Object> INTEGER = (cell,value) -> cell.setCellValue((Integer) value);

    public static final BiConsumer<Cell,Object> DOUBLE = (cell,value) -> cell.setCellValue((Double) value);

    public static final BiConsumer<Cell,Object> FLOAT = (cell,value) -> cell.setCellValue((Float) value);

    public static final BiConsumer<Cell,Object> LONG = (cell,value) -> cell.setCellValue((Long) value);

    public static final BiConsumer<Cell,Object> BOOLEAN = (cell,value) -> cell.setCellValue((Boolean) value);

    public static final BiConsumer<Cell,Object> DATE = (cell,value) -> cell.setCellValue((Date) value);

    public static final BiConsumer<Cell,Object> LOCAL_DATE = (cell,value) -> cell.setCellValue((LocalDate) value);

    public static final BiConsumer<Cell,Object> LOCAL_DATE_TIME = (cell,value) -> cell.setCellValue((LocalDateTime) value);

    public static final BiConsumer<Cell, Object> IMAGE = InitiateExcelStructure::insertImage;



    private void createStructure(int order,Field field){
        switch (field.getType().getName()){
            case "java.lang.Integer","int" -> structures.add(new Structure(field.getName(),order,INTEGER));
            case "java.lang.Double","double" -> structures.add(new Structure(field.getName(),order,DOUBLE));
            case "java.lang.Float","float" -> structures.add(new Structure(field.getName(),order,FLOAT));
            case "java.lang.Long","long" -> structures.add(new Structure(field.getName(),order,LONG));
            case "java.lang.Boolean","boolean" -> structures.add(new Structure(field.getName(),order,BOOLEAN));
            case "java.lang.Date","date" -> structures.add(new Structure(field.getName(),order,DATE));
            case "java.time.LocalDate" -> structures.add(new Structure(field.getName(),order,LOCAL_DATE));
            case "java.time.LocalDateTime" -> structures.add(new Structure(field.getName(),order,LOCAL_DATE_TIME));
            case "java.lang.String" -> structures.add(new Structure(field.getName(),order,TEXT));
            case "java.lang.Byte[]","byte[]","[B" -> structures.add(new Structure(field.getName(),order,IMAGE));
            default -> throw new IllegalStateException("Unexpected value: " + field.getType().getName());
        }
    }

    public InitiateExcelStructure(final Registry registry, final Sheet sheet, final Class<?> clazz) {
        super(registry,sheet);
        registryChildren(clazz);
        registryColorConditional(clazz);
        if(Stream.of(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .allMatch(field -> field.getAnnotation(ExcelColumn.class).position()==0))
        {
            AtomicInteger order = new AtomicInteger(0);
            Stream.of(clazz.getDeclaredFields()).forEach(field -> fieldStyle(field,order.getAndIncrement()));
        }else {
            Stream.of(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                    .forEachOrdered(field -> fieldStyle(field,field.getAnnotation(ExcelColumn.class).position()));
        }
        elements = new Elements(structures,children,conditional);
    }

    private void registryChildren(Class<?> clazz) {
        Stream.of(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Child.class))
                .forEach(field -> children.add(new Children(field.getName(),field.getAnnotation(Child.class).mappedBy(),field.getAnnotation(Child.class).referencedBy())));
    }

    private void registryColorConditional(Class<?> clazz) {
        Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ConditionalExcelCellStyle.class))
                .forEach(this::applyColor);
    }

    private void applyColor(Field field) {
        BiConsumer<Cell,Object> consumer = (cell, value) -> registry.onNext(() -> {
            if(workbook.getNumCellStyles()>=63999) return;
            buildColor(cell, value, field);
        });
        conditional.add(new Conditional(field.getName(), consumer));
    }

    private void buildColor(Cell cell, Object o, Field field) {
        ConditionalExcelCellStyle conditionalExcelCellStyle = field.getAnnotation(ConditionalExcelCellStyle.class);
        Class<? extends ColorCondition> colorConditionClass = conditionalExcelCellStyle.colorCondition();
        Font font = workbook.createFont();
        CellStyle style = workbook.createCellStyle();
        if (Objects.nonNull(colorConditionClass)) {
            try {
                ColorCondition colorCondition = colorConditionClass.getDeclaredConstructor().newInstance();
                createConditionalCellStyle(colorCondition, o, font);
                style.setFont(font);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new StyleException("Failed to create conditional color", e);
            }
        }
        Class<? extends BackgroundColorCondition> backgroundColorConditionClass = conditionalExcelCellStyle.backgroundColorCondition();
        if (Objects.nonNull(backgroundColorConditionClass)) {
            try {
                BackgroundColorCondition backgroundColorCondition = backgroundColorConditionClass.getDeclaredConstructor().newInstance();
                createConditionalCellStyle(backgroundColorCondition, o, style);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new StyleException("Failed to create conditional color", e);
            }
        }
        cell.setCellStyle(style);
    }

    private static void insertImage(Cell cell, Object o) {
        try {
            if(o instanceof Pair<?,?> pair) {
                CellImageHandler.insertImage(cell,(byte[]) pair.getValue(), (Image) pair.getKey());
            }
        } catch (Exception e) {
            throw new StyleException("Failed to insert image", e);
        }
    }

    private void fieldStyle(Field field, int order) {
        if (!field.isAnnotationPresent(ExcelColumn.class)) return;
        ExcelColumn column = field.getAnnotation(ExcelColumn.class);
        createHeaderCell(field, order, column);
        createStructure(order, field);
        validationStatusRegistry(field);
        validationCommentRegistry(field);
    }

    private void createHeaderCell(Field field, int order, ExcelColumn column) {
        Row headerRow = sheet.getLastRowNum() == -1 ? sheet.createRow(0) : sheet.getRow(0);
        Cell cell = headerRow.createCell(order);
        Font font = sheet.getWorkbook().createFont();
        CellStyle style = sheet.getWorkbook().createCellStyle();
        cell.setCellStyle(style);
        CellStyle cellStyle = cell.getCellStyle();
        cell.setCellValue(StringUtils.isBlank(column.name()) ? field.getName() : column.name());
        createStyle(column.headerStyle(), cellStyle, font);
    }

    public Elements getElements() {
        return elements;
    }
}
