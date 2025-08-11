package com.medianet.spamDetection.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "spam_predictions")
@Data
public class EmailRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String emailText;


    private String prediction;

    private LocalDateTime timestamp;

}
