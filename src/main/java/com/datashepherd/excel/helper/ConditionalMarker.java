/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import com.datashepherd.excel.annotation.ValidationComment;
import com.datashepherd.excel.annotation.ValidationStatus;
import com.datashepherd.excel.exception.StyleException;
import com.datashepherd.excel.helper.writer.Conditional;
import com.datashepherd.excel.helper.writer.model.CommentHelper;
import com.datashepherd.excel.helper.writer.model.WritingContext;
import com.datashepherd.excel.helper.writer.style.ConditionalCellStyleHandler;
import com.datashepherd.excel.helper.writer.style.DataStatusConditionHandler;
import com.datashepherd.excel.helper.writer.style.condional.DataStatusCondition;

/**
 * ConditionalMarker handles the registration and application of conditional formatting,
 * validation statuses, and comments in Excel cells.
 */
public class ConditionalMarker implements ConditionalCellStyleHandler, DataStatusConditionHandler {
    protected final List<Conditional> conditional = new ArrayList<>();
    protected final WritingContext context;

    /**
     * Initializes a new instance of ConditionalMarker.
     *
     * @param context the writing context.
     */
    public ConditionalMarker(WritingContext context) {
        this.context = context;
    }

    /**
     * Registers a validation status for a field based on its @ValidationStatus annotation.
     *
     * @param field the Java field.
     */
    protected void registerValidationStatus(Field field) {
        if (field.isAnnotationPresent(ValidationStatus.class)) {
            Class<? extends DataStatusCondition> statusClass = Objects.requireNonNull(field.getAnnotation(ValidationStatus.class))
                                                                      .status();
            BiConsumer<Cell, Object> consumer = (cell, value) -> context.registry().onNext(() -> {
                try {
                    DataStatusCondition condition = statusClass.getDeclaredConstructor().newInstance();
                    CellStyle style = context.styleManager().getOrCreateStatusStyle(condition, value);
                    cell.setCellStyle(style);
                } catch (Exception e) {
                    throw new StyleException("Failed to apply status", e);
                }
            });
            conditional.add(new Conditional(field.getName(), consumer));
        }
    }

    /**
     * Registers a validation comment for a field based on its @ValidationComment annotation.
     *
     * @param field the Java field.
     */
    protected void registerValidationComment(Field field) {
        if (field.isAnnotationPresent(ValidationComment.class)) {
            CommentHelper commentHelper = CommentHelper.getInstance(context.sheet());
            BiConsumer<Cell, Object> consumer = (cellComment, value) -> {
                try {
                    commentHelper.writeComment(cellComment, Objects.requireNonNull(field.getAnnotation(ValidationComment.class)).comment().getDeclaredConstructor().newInstance().applyCondition(value));
                } catch (Exception e) {
                    throw new StyleException("Failed to apply comment", e);
                }
            };
            conditional.add(new Conditional(field.getName(), consumer));
        }
    }
}
