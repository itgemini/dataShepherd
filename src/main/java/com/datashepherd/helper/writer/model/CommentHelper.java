/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Helper class to add comment to cell.
 */
public class CommentHelper extends Anchor {
    private static CommentHelper instance = null;
    public static synchronized CommentHelper getInstance(Sheet sheet){
        if(instance == null) instance = new CommentHelper(sheet);
        return instance;
    }
    private CommentHelper(Sheet sheet) {
        super(sheet);

    }
    public void writeComment(Cell cell, String text){
        if(StringUtils.isBlank(text)) return;
        clientAnchor.setCol1(cell.getColumnIndex());
        clientAnchor.setCol2(Math.addExact(cell.getColumnIndex() , 1));
        clientAnchor.setRow1(cell.getRow().getRowNum());
        clientAnchor.setRow2(Math.addExact(cell.getRow().getRowNum() , 1));
        Comment comment = drawing.createCellComment(clientAnchor);
        comment.setString(factory.createRichTextString(text));
        cell.setCellComment(comment);
    }
}
