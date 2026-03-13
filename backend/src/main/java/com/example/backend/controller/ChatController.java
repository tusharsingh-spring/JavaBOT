package com.example.backend.controller;

import com.example.backend.payload.CricketResponse;
import com.example.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @SuppressWarnings("NullableProblems")
    @GetMapping
    public ResponseEntity<String> generateResponse(@RequestParam("inputText") String inputText) {
        String responseText = chatService.generateResponse(inputText);
        return ResponseEntity.ok(responseText);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamResponse(@RequestParam("inputText") String inputText) {
        return chatService.streamResponse(inputText)
                .onErrorResume(error -> {
                    System.err.println("Stream error: " + error.getMessage());
                    return Flux.just("Error: Unable to stream response - " + error.getMessage());
                });
    }

    @GetMapping("/cricket-bot")
    public ResponseEntity<CricketResponse> getCricketResponse(@RequestParam("inputText") String inputText) {
            CricketResponse cricketResponse = chatService.generateCricketResponse(inputText);
        return ResponseEntity.ok(cricketResponse);
    }
}