package com.medianet.spamDetection.service.impl;

import com.medianet.spamDetection.entity.EmailRecord;
import com.medianet.spamDetection.repository.EmailRecordRepository;
import com.medianet.spamDetection.service.interfaces.ISpamDetectionService;
import com.medianet.spamDetection.utils.EmailReader;
import com.medianet.spamDetection.utils.ExcelExport;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
@AllArgsConstructor
public class SpamDetectionServiceImpl implements ISpamDetectionService {

    private EmailRecordRepository repo;
    private WebClient webClient;
    private EmailReader emailReader;
    private ExcelExport excelExportService;


    @Scheduled(cron = "0 0 18 * * *")   //Every day at 18:00:00
//   @Scheduled(fixedRate = 500000)
    public void processEmails() {
        System.out.println("Processing emails...");
        List<String> newEmailContents = emailReader.checkInbox();
        if (newEmailContents.isEmpty()) return;
        for (String emailContent : newEmailContents) {
            String prediction = callModel(emailContent);
            System.out.println(prediction );

            EmailRecord record = new EmailRecord();
            record.setEmailText(emailContent);
            record.setPrediction(prediction);
            record.setTimestamp(LocalDateTime.now());
            repo.save(record);
        }

        // After processing all emails, generate the Excel report
        try {
            List<EmailRecord> allRecords = repo.findAll();
            String filePath = "../spam_predictions_report.xlsx";
            excelExportService.exportSpamResultsToExcel(allRecords, filePath);
            System.out.println("Excel report generated at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String callModel(String emailContent) {
        Map<String, String> requestBody = Map.of("email", emailContent);

        return webClient.post()
                .uri("/predict")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("result"))
                .block();
    }
}
