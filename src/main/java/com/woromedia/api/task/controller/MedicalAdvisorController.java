
package com.woromedia.api.task.controller;

import com.woromedia.api.task.service.OpenAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical")
@CrossOrigin("*")
public class MedicalAdvisorController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalAdvisorController.class);
    private final OpenAIService openAIService;

    public MedicalAdvisorController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/advice")
    public ResponseEntity<?> getAdvice(@RequestParam String symptom) {
        logger.info("Received request for symptom: {}", symptom);
        try {
            String response = openAIService.getMedicalAdvice(symptom);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching medical advice: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("{\"error\": \"Unable to process request at the moment.\"}");
        }
    }
}
