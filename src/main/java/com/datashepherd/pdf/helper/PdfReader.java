package com.datashepherd.pdf.helper;


import com.datashepherd.pdf.PDFException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.FileInputStream;

public class PdfReader {
    private PdfReader() {
    }

    public static <T> T read(String path, Class<T> clazz) {
        try (FileInputStream fis = new FileInputStream(path);
             PDDocument document = Loader.loadPDF(fis.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String content = stripper.getText(document);
            return PdfObjectMapper.map(content, clazz);
        } catch (Exception e) {
            throw new PDFException("Failed to read PDF", e);
        }
    }
}
