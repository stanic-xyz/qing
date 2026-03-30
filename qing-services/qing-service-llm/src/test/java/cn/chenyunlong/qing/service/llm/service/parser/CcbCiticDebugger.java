package cn.chenyunlong.qing.service.llm.service.parser;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class CcbCiticDebugger {
    @Test
    public void testCcbPdf() throws Exception {
        System.out.println("=== CCB PDF ===");
        Path dir = Paths.get("src/test/resources/bills/理财信息/银行流水记录/建设银行/pdf");
        Optional<Path> testFile = Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".pdf"))
                .findFirst();

        if (testFile.isPresent()) {
            try (PDDocument document = Loader.loadPDF(testFile.get().toFile())) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                String[] lines = text.split("\\r?\\n");
                for (int i = 0; i < Math.min(50, lines.length); i++) {
                    System.out.println("LINE " + i + ": " + lines[i]);
                }
            }
        }
    }
    
    @Test
    public void testCiticXls() throws Exception {
        System.out.println("=== CITIC XLS ===");
        Path dir = Paths.get("src/test/resources/bills/理财信息/银行流水记录/中信银行信用卡");
        Optional<Path> testFile = Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".xls"))
                .findFirst();

        if (testFile.isPresent()) {
            try (FileInputStream fis = new FileInputStream(testFile.get().toFile());
                 Workbook workbook = new HSSFWorkbook(fis)) {
                 Sheet sheet = workbook.getSheetAt(0);
                 for (int i = 0; i < Math.min(30, sheet.getLastRowNum() + 1); i++) {
                     Row row = sheet.getRow(i);
                     if (row == null) continue;
                     StringBuilder sb = new StringBuilder("ROW " + i + ": ");
                     for (Cell cell : row) {
                         sb.append(cell.toString()).append(" | ");
                     }
                     System.out.println(sb.toString());
                 }
            }
        }
    }
}