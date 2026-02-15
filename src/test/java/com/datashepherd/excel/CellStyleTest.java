package com.datashepherd.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import com.datashepherd.excel.annotation.Cell;
import com.datashepherd.excel.annotation.Sheet;
import com.datashepherd.excel.service.WriterService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellStyleTest {

    @Test
    public void testCellStyleInCover() throws IOException {
        StyledCover cover = new StyledCover();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        WriterService writerService = new WriterService().xlsx();
        writerService.cover(cover);
        byte[] content = writerService.content();

        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(content));
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet("StyleTest");

        // Check styled cell (1, 1) -> Row 1, Col 1
        Row row1 = sheet.getRow(1);
        org.apache.poi.ss.usermodel.Cell cell1 = row1.getCell(1);
        assertEquals("Hello Style", cell1.getStringCellValue());

        CellStyle style1 = cell1.getCellStyle();
        org.apache.poi.ss.usermodel.Font font1 = workbook.getFontAt(style1.getFontIndex());

        // Color RED in my Color enum corresponds to some index.
        // ExcelStyleManager uses IndexedColors.valueOf(fontAnnotation.color().name().toUpperCase()).getIndex()
        assertEquals(IndexedColors.RED.getIndex(), font1.getColor());
        assertEquals(14, font1.getFontHeightInPoints());
        assertEquals(FillPatternType.SOLID_FOREGROUND, style1.getFillPattern());
        assertEquals(IndexedColors.YELLOW.getIndex(), style1.getFillForegroundColor());

        // Check default styled cell (2, 1) -> Row 2, Col 1
        Row row2 = sheet.getRow(2);
        org.apache.poi.ss.usermodel.Cell cell2 = row2.getCell(1);
        assertEquals("Default Style", cell2.getStringCellValue());

        CellStyle style2 = cell2.getCellStyle();
        // Default in @Cell is WHITE background, NO_FILL, BLACK 11pt font
        assertEquals(FillPatternType.NO_FILL, style2.getFillPattern());
        org.apache.poi.ss.usermodel.Font font2 = workbook.getFontAt(style2.getFontIndex());
        assertEquals(IndexedColors.BLACK.getIndex(), font2.getColor());
        assertEquals(11, font2.getFontHeightInPoints());

        workbook.close();
    }

    @Sheet(name = "StyleTest")
    public static class StyledCover {
        @Cell(row = 1, column = 1)
        private final String styledText = "Hello Style";

        @Cell(row = 2, column = 1) // Default style
        private final String defaultText = "Default Style";
    }
}
