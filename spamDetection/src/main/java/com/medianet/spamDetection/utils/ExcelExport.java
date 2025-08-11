package com.medianet.spamDetection.utils;

import com.medianet.spamDetection.entity.EmailRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExport {

    public void exportSpamResultsToExcel(List<EmailRecord> records, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Spam Predictions");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Email Text");
        headerRow.createCell(1).setCellValue("Prediction");
        headerRow.createCell(2).setCellValue("Timestamp");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int rowNum = 1;
        for (EmailRecord record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getEmailText());
            row.createCell(1).setCellValue(record.getPrediction());
            row.createCell(2).setCellValue(record.getTimestamp().format(formatter));
        }

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
