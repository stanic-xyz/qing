package cn.chenyunlong.qing.service.llm.service.parser;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class BocCreditPdfDebugger {
    @Test
    public void test() throws Exception {
        Path dir = Paths.get("src/test/resources/bills/理财信息/银行流水记录/中国银行信用卡");
        Optional<Path> testFile = Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith("2023年08月账单补制.PDF"))
                .findFirst();

        if (testFile.isEmpty()) {
            System.out.println("找不到指定的测试文件");
            return;
        }

        File file = testFile.get().toFile();
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            String[] lines = text.split("\\r?\\n");
            for (int i = 0; i < Math.min(100, lines.length); i++) {
                System.out.println("LINE " + i + ": " + lines[i]);
            }
        }
    }
}