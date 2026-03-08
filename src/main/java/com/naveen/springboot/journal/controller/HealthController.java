package com.naveen.springboot.journal.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

  @Autowired
  private MongoTemplate mongoTemplate;

  @GetMapping("/mongodb")
  public ResponseEntity<Map<String, String>> checkMongoDBHealth() {
    Map<String, String> status = new HashMap<>();
    try {
      // Simple ping to check connection
      mongoTemplate.getCollection("test").countDocuments();
      status.put("status", "UP");
      status.put("database", "Connected");
      return ResponseEntity.ok(status);
    } catch (Exception e) {
      status.put("status", "DOWN");
      status.put("error", e.getMessage());
      return ResponseEntity.status(503).body(status);
    }
  }
}

