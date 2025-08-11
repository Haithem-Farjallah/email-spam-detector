package com.medianet.spamDetection.controller;

import com.medianet.spamDetection.service.interfaces.ISpamDetectionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class SpamDetectionController {
    private ISpamDetectionService spamDetectionService;
}
