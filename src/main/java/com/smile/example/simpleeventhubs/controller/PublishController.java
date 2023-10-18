package com.smile.example.simpleeventhubs.controller;

import com.smile.example.simpleeventhubs.service.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublishController {
    private final ObjectMapper obj;
    private final Producer producer;

    @PostMapping(value = "/publish")
    public ResponseEntity<Object> publishMessage(@RequestBody Object request) throws JsonProcessingException {
        producer.publishMessage("simple-topic", obj.writeValueAsString(request));
        return ResponseEntity.ok().body("success");
    }
}
