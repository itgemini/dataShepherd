/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.model;

import com.datashepherd.annotation.Picture;
import com.datashepherd.exception.PictureException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class SheetPictureHandler extends Anchor {
    public SheetPictureHandler(Sheet sheet,Class<?> clazz) {
        super(sheet);
        if(!clazz.isAnnotationPresent(com.datashepherd.annotation.Sheet.class) || Objects.isNull(clazz.getAnnotation(com.datashepherd.annotation.Sheet.class).picture()) || StringUtils.isBlank(clazz.getAnnotation(com.datashepherd.annotation.Sheet.class).picture().path())) return;
        Picture picture = clazz.getAnnotation(com.datashepherd.annotation.Sheet.class).picture();
        clientAnchor.setCol1(picture.startColumn());
        clientAnchor.setRow1(picture.startRow());
        clientAnchor.setCol2(picture.endColumn());
        clientAnchor.setRow2(picture.endRow());
        try(FileInputStream image = new FileInputStream(picture.path())) {
            byte[] imageBytes = IOUtils.toByteArray(image);
            org.apache.poi.ss.usermodel.Picture photo = drawing.createPicture(clientAnchor, sheet.getWorkbook().addPicture(imageBytes, picture.extension().getTypeIndex()));
            photo.resize();
        } catch (IOException e) {
            throw new PictureException(e.getMessage(),e);
        }
    }
}
