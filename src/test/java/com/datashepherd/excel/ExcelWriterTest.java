package com.datashepherd.excel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import com.datashepherd.excel.service.WriterService;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExcelWriterTest {

    private static final String OUTPUT_DIR = "src/test/resources/write/scenarios";

    @Test
    public void testWriteCoverWithStyle() throws IOException {
        String filename = OUTPUT_DIR + "/write_cover_with_style.xlsx";
        MockDataGenerator.CoverEntity cover = new MockDataGenerator.CoverEntity();

        new WriterService()
                .xlsx()
                .cover(cover)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testWriteProfileWithImageAndStyle() throws IOException {
        String filename = OUTPUT_DIR + "/write_profile_with_image_and_style.xlsx";
        List<MockDataGenerator.ProfileEntity> data = MockDataGenerator.generateProfileData(5);

        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.ProfileEntity.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testWriteWithFormattedColumns() throws IOException {
        String filename = OUTPUT_DIR + "/write_with_formatted_columns.xlsx";
        List<MockDataGenerator.FormattedEntity> data = MockDataGenerator.generateFormattedData(10);

        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.FormattedEntity.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testWriteWithMultiColumnStyles() throws IOException {
        String filename = OUTPUT_DIR + "/write_with_multi_column_styles.xlsx";
        List<MockDataGenerator.MultiStyledEntity> data = MockDataGenerator.generateMultiStyledData(10);

        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.MultiStyledEntity.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testWriteWithStyle() throws IOException {
        String filename = OUTPUT_DIR + "/write_with_style.xlsx";
        List<MockDataGenerator.StyledEntity> data = MockDataGenerator.generateStyledData(10);

        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.StyledEntity.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testBasicWrite() throws IOException {
        String filename = OUTPUT_DIR + "/basic_write.xlsx";
        List<MockDataGenerator.SimpleEntity> data = MockDataGenerator.generateSimpleData(10);

        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.SimpleEntity.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testWriteWithCover() throws IOException {
        String filename = OUTPUT_DIR + "/write_with_cover.xlsx";
        List<MockDataGenerator.SimpleEntity> data = MockDataGenerator.generateSimpleData(10);
        MockDataGenerator.CoverEntity cover = new MockDataGenerator.CoverEntity();

        new WriterService()
                .xlsx()
                .cover(cover)
                .writeToExcel(data, MockDataGenerator.SimpleEntity.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testWriteWithImages() throws IOException {
        String filename = OUTPUT_DIR + "/write_with_images.xlsx";
        List<MockDataGenerator.EntityWithImage> data = MockDataGenerator.generateImageData(5);

        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.EntityWithImage.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testWriteWithRelationships() throws IOException {
        String filename = OUTPUT_DIR + "/write_with_relationships.xlsx";
        List<MockDataGenerator.ParentEntity> data = MockDataGenerator.generateRelationalData(3, 4);

        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.ParentEntity.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testComprehensiveWrite() throws IOException {
        String filename = OUTPUT_DIR + "/comprehensive_write.xlsx";
        List<MockDataGenerator.ParentEntity> data = MockDataGenerator.generateRelationalData(5, 2);
        MockDataGenerator.CoverEntity cover = new MockDataGenerator.CoverEntity();

        new WriterService()
                .xlsx()
                .cover(cover)
                .writeToExcel(data, MockDataGenerator.ParentEntity.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    public void testProfilePhotoColumn() throws IOException {
        String filename = OUTPUT_DIR + "/profile_photo_column.xlsx";
        List<MockDataGenerator.EntityWithImage> data = MockDataGenerator.generateImageData(3);

        new WriterService()
                .xlsx()
                .writeToExcel(data, MockDataGenerator.EntityWithImage.class)
                .saveExcelTo(filename);

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    @Disabled("Performance test - enable when needed")
    public void testOneMillionRecordsPerformance() throws IOException {
        String filename = OUTPUT_DIR + "/one_million_records.xlsx";
        int count = 1000000;

        // Using a list for now, but 1M objects might be heavy for the heap.
        // Let's see if 1M SimpleEntity fits in memory. 
        // SimpleEntity has an int and a String. ~50-100 bytes per object.
        // 1M * 100 = 100MB. Should be fine.
        List<MockDataGenerator.SimpleEntity> data = MockDataGenerator.generateSimpleData(count);

        long start = System.currentTimeMillis();

        new WriterService()
                .xlsx()
                .xlsxLarge() // Mandatory for 1M records to avoid OOM in POI
                .writeToExcel(data, MockDataGenerator.SimpleEntity.class)
                .saveExcelTo(filename);

        long end = System.currentTimeMillis();
        System.out.println("Time to write 1M records: " + (end - start) + "ms");

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }

    @Test
    @Disabled("Performance test - enable when needed")
    public void testOneMillionRecordsRelationalPerformance() throws IOException {
        String filename = OUTPUT_DIR + "/one_million_records_relational.xlsx";
        int parentCount = 500000;
        int childrenPerParent = 2; // Total 1.5M records across 2 sheets

        // This might be pushing memory limits if we hold all in a List
        // ParentEntity: id(4), name(ref 4), children(ref 4) + overhead
        // ChildEntity: id(4), name(ref 4), parentId(4) + overhead
        // 500k parents + 1M children = 1.5M objects.
        // Let's try it.
        List<MockDataGenerator.ParentEntity> data = MockDataGenerator.generateRelationalData(parentCount, childrenPerParent);

        long start = System.currentTimeMillis();

        new WriterService()
                .xlsx()
                .xlsxLarge()
                .writeToExcel(data, MockDataGenerator.ParentEntity.class)
                .saveExcelTo(filename);

        long end = System.currentTimeMillis();
        System.out.println("Time to write 1.5M relational records: " + (end - start) + "ms");

        assertTrue(Files.exists(Paths.get(filename + ".xlsx")) || Files.exists(Paths.get(filename)));
    }
}
