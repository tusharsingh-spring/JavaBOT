package com.example.backend.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import com.example.backend.payload.CricketResponse;

@Service
public class ChatService {

    @Autowired
    private ChatModel chatModel;

    // 👇 REMOVE THIS ENTIRE LINE
    // @Autowired
    // private ObjectMapper objectMapper;

    public String generateResponse(String inputText) {
        return chatModel.call(inputText);
    }

    public Flux<String> streamResponse(String inputText) {
        return chatModel.stream(inputText);
    }

    public CricketResponse generateCricketResponse(String inputText) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String promptString = "As Cricket Expert, answer this question: " + inputText +
                    "\nIf the above question is not related to cricket, generate a joke and link it to cricket." +
                    "\nIMPORTANT: You MUST respond with a VALID JSON object that has ONLY ONE field called 'content'." +
                    "\nExample: {\"content\": \"Your answer here\"}" +
                    "\nDo not include any other text, explanations, or markdown formatting. ONLY return the JSON object.";

            String responseString = chatModel.call(new Prompt(promptString))
                    .getResult()
                    .getOutput()
                    .getText();

            System.out.println("Raw response: " + responseString); // For debugging

            // Try to parse as JSON first
            try {
                return objectMapper.readValue(responseString, CricketResponse.class);
            } catch (JsonParseException e) {
                // If JSON parsing fails, wrap the text in a CricketResponse object
                CricketResponse fallbackResponse = new CricketResponse();
                fallbackResponse.setContent(responseString);
                return fallbackResponse;
            }

        } catch (Exception e) {
            e.printStackTrace();
            CricketResponse errorResponse = new CricketResponse();
            errorResponse.setContent("Error processing request: " + e.getMessage());
            return errorResponse;
        }
    }
}