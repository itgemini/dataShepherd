/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import com.datashepherd.excel.annotation.Picture;
import com.datashepherd.excel.exception.PictureException;

public class SheetPictureHandler extends Anchor {
    public SheetPictureHandler(Sheet sheet,Class<?> clazz) {
        super(sheet);
        if (!clazz.isAnnotationPresent(com.datashepherd.excel.annotation.Sheet.class) || Objects.isNull(clazz.getAnnotation(com.datashepherd.excel.annotation.Sheet.class).picture()) || StringUtils.isBlank(clazz.getAnnotation(com.datashepherd.excel.annotation.Sheet.class).picture().path()))
            return;
        Picture picture = clazz.getAnnotation(com.datashepherd.excel.annotation.Sheet.class).picture();
        clientAnchor.setCol1(picture.startColumn());
        clientAnchor.setRow1(picture.startRow());
        clientAnchor.setCol2(picture.endColumn());
        clientAnchor.setRow2(picture.endRow());
        try(FileInputStream image = new FileInputStream(picture.path())) {
            byte[] imageBytes = IOUtils.toByteArray(image);
            org.apache.poi.ss.usermodel.Picture photo = drawing.createPicture(clientAnchor, sheet.getWorkbook().addPicture(imageBytes, picture.extension().getTypeIndex()));
            photo.resize(1.0);

            // Center the image in the specified range
            double totalWidth = 0;
            for (int col = picture.startColumn(); col < picture.endColumn(); col++) {
                totalWidth += sheet.getColumnWidthInPixels(col);
            }
            double totalHeight = 0;
            for (int row = picture.startRow(); row < picture.endRow(); row++) {
                totalHeight += (sheet.getRow(row) != null ? sheet.getRow(row)
                                                                 .getHeightInPoints() : sheet.getDefaultRowHeightInPoints()) * 1.33;
            }

            double imgWidth = photo.getImageDimension().getWidth();
            double imgHeight = photo.getImageDimension().getHeight();

            int dx = (int) Math.max(0, (totalWidth - imgWidth) / 2 * 9525);
            int dy = (int) Math.max(0, (totalHeight - imgHeight) / 2 * 9525);

            clientAnchor.setDx1(dx);
            clientAnchor.setDy1(dy);
        } catch (IOException e) {
            throw new PictureException(e.getMessage(),e);
        }
    }
}
