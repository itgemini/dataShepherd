/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import com.datashepherd.excel.annotation.Child;
import com.datashepherd.excel.annotation.ExcelColumn;
import com.datashepherd.excel.annotation.style.ConditionalExcelCellStyle;
import com.datashepherd.excel.exception.StyleException;
import com.datashepherd.excel.helper.Children;
import com.datashepherd.excel.helper.ConditionalMarker;
import com.datashepherd.excel.helper.ExcelMetadataReader;
import com.datashepherd.excel.helper.writer.model.TypeProcessorRegistry;
import com.datashepherd.excel.helper.writer.model.WritingContext;
import com.datashepherd.excel.helper.writer.style.condional.BackgroundColorCondition;
import com.datashepherd.excel.helper.writer.style.condional.ColorCondition;

/**
 * Helper class to initialize the Excel structure for writing data.
 * It manages the mapping between Java fields and Excel columns, handles styles,
 * and sets up conditional formatting.
 */
public class InitiateExcelStructure extends ConditionalMarker {
    private final List<Structure> structures = new ArrayList<>();
    private final List<Children> children = new ArrayList<>();
    private final Elements elements;

    /**
     * Processor for String values.
     */
    public static final BiConsumer<Cell, Object> TEXT = TypeProcessorRegistry.TEXT;

    /**
     * Processor for Integer values.
     */
    public static final BiConsumer<Cell, Object> INTEGER = TypeProcessorRegistry.INTEGER;

    /**
     * Processor for Double values.
     */
    public static final BiConsumer<Cell, Object> DOUBLE = TypeProcessorRegistry.DOUBLE;

    /**
     * Processor for Float values.
     */
    public static final BiConsumer<Cell, Object> FLOAT = TypeProcessorRegistry.FLOAT;

    /**
     * Processor for Long values.
     */
    public static final BiConsumer<Cell, Object> LONG = TypeProcessorRegistry.LONG;

    /**
     * Processor for Boolean values.
     */
    public static final BiConsumer<Cell, Object> BOOLEAN = TypeProcessorRegistry.BOOLEAN;

    /**
     * Processor for Date values.
     */
    public static final BiConsumer<Cell, Object> DATE = TypeProcessorRegistry.DATE;

    /**
     * Processor for LocalDate values.
     */
    public static final BiConsumer<Cell, Object> LOCAL_DATE = TypeProcessorRegistry.LOCAL_DATE;

    /**
     * Processor for LocalDateTime values.
     */
    public static final BiConsumer<Cell, Object> LOCAL_DATE_TIME = TypeProcessorRegistry.LOCAL_DATE_TIME;

    /**
     * Processor for Image values.
     */
    public static final BiConsumer<Cell, Object> IMAGE = TypeProcessorRegistry.IMAGE;

    /**
     * Initializes a new instance of InitiateExcelStructure.
     *
     * @param context the writing context.
     * @param clazz   the class of the objects to be written.
     */
    public InitiateExcelStructure(WritingContext context, final Class<?> clazz) {
        super(context);
        registerChildren(clazz);
        registerColorConditional(clazz);
        com.datashepherd.excel.annotation.Sheet sheetAnn = ExcelMetadataReader.getSheetAnnotation(clazz);
        int headerRowNum = sheetAnn.headerRow();
        Row headerRow = context.sheet().getRow(headerRowNum) == null ? context.sheet()
                                                                              .createRow(headerRowNum) : context.sheet()
                                                                                                                .getRow(headerRowNum);

        if (ExcelMetadataReader.hasAllDefaultPositions(clazz)) {
            AtomicInteger order = new AtomicInteger(0);
            Stream.of(clazz.getDeclaredFields())
                  .forEach(field -> fieldStyle(field, order.getAndIncrement(), headerRow));
        }
        else {
            ExcelMetadataReader.getExcelColumnFields(clazz)
                               .forEachOrdered(field -> fieldStyle(field, field.getAnnotation(ExcelColumn.class)
                                                                               .position(), headerRow));
        }
        elements = new Elements(structures, children, conditional, headerRowNum);
    }

    /**
     * Creates the internal structure for a field.
     *
     * @param order the column position.
     * @param field the Java field.
     */
    private void createStructure(int order, Field field) {
        BiConsumer<Cell, Object> processor = TypeProcessorRegistry.getProcessor(field.getType().getName());
        if (processor != null) {
            structures.add(new Structure(field.getName(), order, processor));
        }
        else {
            throw new IllegalStateException("Unexpected value: " + field.getType().getName());
        }
    }

    /**
     * Registers children sheets for master-detail relationships.
     *
     * @param clazz the entity class.
     */
    private void registerChildren(Class<?> clazz) {
        ExcelMetadataReader.getChildFields(clazz)
                           .forEach(field -> children.add(new Children(field.getName(), field.getAnnotation(Child.class)
                                                                                             .mappedBy(), field.getAnnotation(Child.class)
                                                                                                               .referencedBy())));
    }

    /**
     * Registers conditional cell styles based on field values.
     *
     * @param clazz the entity class.
     */
    private void registerColorConditional(Class<?> clazz) {
        Stream.of(clazz.getDeclaredFields())
              .filter(field -> field.isAnnotationPresent(ConditionalExcelCellStyle.class))
              .forEach(this::applyColor);
    }

    /**
     * Applies conditional color to a field.
     *
     * @param field the Java field.
     */
    private void applyColor(Field field) {
        BiConsumer<Cell, Object> consumer = (cell, value) -> context.registry()
                                                                    .onNext(() -> buildColor(cell, value, field));
        conditional.add(new Conditional(field.getName(), consumer));
    }

    /**
     * Builds and applies the conditional color to a cell.
     *
     * @param cell  the Excel cell.
     * @param o     the field value.
     * @param field the Java field.
     */
    private void buildColor(Cell cell, Object o, Field field) {
        ConditionalExcelCellStyle conditionalExcelCellStyle = field.getAnnotation(ConditionalExcelCellStyle.class);
        Class<? extends ColorCondition> colorConditionClass = conditionalExcelCellStyle.colorCondition();
        Class<? extends BackgroundColorCondition> backgroundColorConditionClass = conditionalExcelCellStyle.backgroundColorCondition();

        if (Objects.nonNull(colorConditionClass) || Objects.nonNull(backgroundColorConditionClass)) {
            try {
                ColorCondition colorCondition = Objects.nonNull(colorConditionClass) ? colorConditionClass.getDeclaredConstructor()
                                                                                                          .newInstance() : null;
                BackgroundColorCondition backgroundColorCondition = Objects.nonNull(backgroundColorConditionClass) ? backgroundColorConditionClass.getDeclaredConstructor()
                                                                                                                                                  .newInstance() : null;
                CellStyle style = context.styleManager()
                                         .getOrCreateConditionalStyle(colorCondition, backgroundColorCondition, o);
                cell.setCellStyle(style);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new StyleException("Failed to create conditional color", e);
            }
        }
    }

    /**
     * Applies style to a field and registers it in the internal structure.
     *
     * @param field     the Java field.
     * @param order     the column position.
     * @param headerRow the header row.
     */
    private void fieldStyle(Field field, int order, Row headerRow) {
        if (!field.isAnnotationPresent(ExcelColumn.class)) return;
        ExcelColumn column = field.getAnnotation(ExcelColumn.class);
        fillHeaderCell(field, order, column, headerRow);
        createStructure(order, field);
        registerValidationStatus(field);
        registerValidationComment(field);
    }

    /**
     * Fills a header cell with title and style.
     *
     * @param field     the Java field.
     * @param order     the column position.
     * @param column    the Excel column annotation.
     * @param headerRow the header row.
     */
    private void fillHeaderCell(Field field, int order, ExcelColumn column, Row headerRow) {
        Cell cell = headerRow.createCell(order);
        cell.setCellValue(StringUtils.isBlank(column.name()) ? field.getName() : column.name());
        CellStyle style = context.styleManager().getOrCreateStyle(column.headerStyle(), null);
        cell.setCellStyle(style);
    }

    /**
     * Returns the initialized elements of the Excel structure.
     * @return the Excel elements.
     */
    public Elements getElements() {
        return elements;
    }
}
