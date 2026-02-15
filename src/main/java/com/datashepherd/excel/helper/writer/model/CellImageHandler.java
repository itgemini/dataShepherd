/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.model;

import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import com.datashepherd.excel.annotation.Image;

public class CellImageHandler {
    private CellImageHandler() {}
    public static void insertImage(Cell cell, byte[] imageBytes, Image image) {
        if(Objects.isNull(imageBytes)) return;
        Drawing<?> drawing = cell.getSheet().createDrawingPatriarch();
        ClientAnchor clientAnchor =  cell.getSheet().getWorkbook().getCreationHelper().createClientAnchor();
        clientAnchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
        clientAnchor.setCol1(cell.getColumnIndex());
        clientAnchor.setRow1(cell.getRowIndex());
        clientAnchor.setCol2(cell.getColumnIndex() + 1);
        clientAnchor.setRow2(cell.getRowIndex() + 1);
        org.apache.poi.ss.usermodel.Picture photo = drawing.createPicture(clientAnchor, cell.getSheet().getWorkbook().addPicture(imageBytes, image.extension().getTypeIndex()));
        cell.getSheet().setColumnWidth(cell.getColumnIndex(), image.width() * 32);
        cell.getRow().setHeightInPoints(image.height());
        photo.resize(1.0);

        // Center the image in the cell
        double cellWidth = cell.getSheet().getColumnWidthInPixels(cell.getColumnIndex());
        double cellHeight = cell.getRow().getHeightInPoints() * 1.33; // Approx points to pixels
        double imgWidth = photo.getImageDimension().getWidth();
        double imgHeight = photo.getImageDimension().getHeight();

        int dx = (int) Math.max(0, (cellWidth - imgWidth) / 2 * 9525); // pixels to EMU (approx)
        int dy = (int) Math.max(0, (cellHeight - imgHeight) / 2 * 9525);

        clientAnchor.setDx1(dx);
        clientAnchor.setDy1(dy);
    }
}
