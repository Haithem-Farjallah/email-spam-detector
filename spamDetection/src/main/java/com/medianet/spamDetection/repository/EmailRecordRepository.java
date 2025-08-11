package com.medianet.spamDetection.repository;

import com.medianet.spamDetection.entity.EmailRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;


public interface EmailRecordRepository extends JpaRepository<EmailRecord, Long> {
    @Query("SELECT MAX(e.timestamp) FROM EmailRecord e")
    LocalDateTime findMaxTimestamp();
}
