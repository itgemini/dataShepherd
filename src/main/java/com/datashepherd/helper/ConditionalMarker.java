/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper;


import com.datashepherd.annotation.ValidationComment;
import com.datashepherd.annotation.ValidationStatus;
import com.datashepherd.exception.StyleException;
import com.datashepherd.helper.writer.Conditional;
import com.datashepherd.helper.writer.model.CommentHelper;
import com.datashepherd.helper.writer.style.ConditionalCellStyleHandler;
import com.datashepherd.helper.writer.style.DataStatusConditionHandler;
import com.datashepherd.helper.writer.style.ExcelStyleHandler;
import com.datashepherd.helper.writer.style.condional.DataStatusCondition;
import com.datashepherd.helper.writer.style.condional.Registry;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ConditionalMarker implements ExcelStyleHandler, ConditionalCellStyleHandler, DataStatusConditionHandler {
    protected final List<Conditional> conditional = new ArrayList<>();
    protected final Registry registry;
    protected final Workbook workbook;
    protected final Sheet sheet;
    public ConditionalMarker(final Registry registry, final Sheet sheet) {
        this.registry = registry;
        this.workbook = sheet.getWorkbook();
        this.sheet = sheet;
    }

    protected void validationStatusRegistry(Field field) {
        if (field.isAnnotationPresent(ValidationStatus.class)) {
            BiConsumer<Cell, Object> consumer = (conditionalCell, value) -> registry.onNext(() -> {
                if(workbook.getNumCellStyles()>=63999) return;
                CellStyle style = workbook.createCellStyle();
                conditionalCell.setCellStyle(style);
                CellStyle cellStyle = conditionalCell.getCellStyle();
                Class<? extends DataStatusCondition> conditionalStatusClass = field.getAnnotation(ValidationStatus.class).status();
                try {
                    DataStatusCondition dataStatusCondition = conditionalStatusClass.getDeclaredConstructor().newInstance();
                    applyStatus(dataStatusCondition, value, cellStyle);
                } catch (Exception e) {
                    throw new StyleException("Failed to apply status", e);
                }
            });
            conditional.add(new Conditional(field.getName(), consumer));
        }
    }

    protected void validationCommentRegistry(Field field) {
        if (field.isAnnotationPresent(ValidationComment.class)) {
            CommentHelper commentHelper = CommentHelper.getInstance(sheet);
            BiConsumer<Cell, Object> consumer = (cellComment, value) -> {
                try {
                    commentHelper.writeComment(cellComment, field.getAnnotation(ValidationComment.class).comment().getDeclaredConstructor().newInstance().applyCondition(value));
                } catch (Exception e) {
                    throw new StyleException("Failed to apply comment", e);
                }
            };
            conditional.add(new Conditional(field.getName(), consumer));
        }
    }
}
