/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.model;

import com.datashepherd.annotation.Image;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;

import java.util.Objects;

public class CellImageHandler {
    private CellImageHandler() {}
    public static void insertImage(Cell cell, byte[] imageBytes, Image image) {
        if(Objects.isNull(imageBytes)) return;
        Drawing<?> drawing = cell.getSheet().createDrawingPatriarch();
        ClientAnchor clientAnchor =  cell.getSheet().getWorkbook().getCreationHelper().createClientAnchor();
        clientAnchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
        clientAnchor.setCol1(cell.getColumnIndex());
        clientAnchor.setRow1(cell.getRowIndex());
        org.apache.poi.ss.usermodel.Picture photo = drawing.createPicture(clientAnchor, cell.getSheet().getWorkbook().addPicture(imageBytes, image.extension().getTypeIndex()));
        cell.getSheet().setColumnWidth(cell.getColumnIndex(), image.width() * 36);
        cell.getSheet().getRow(cell.getRowIndex()).setHeight((short) (image.height() * 20));
        photo.resize();
    }
}
