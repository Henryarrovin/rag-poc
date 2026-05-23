package com.henry.rag.api.controller;

import com.henry.rag.api.dto.ChatRequest;
import com.henry.rag.api.dto.ChatResponse;
import com.henry.rag.rag.retrieval.RagChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final RagChatService ragChatService;

    public ChatController(RagChatService ragChatService) {
        this.ragChatService = ragChatService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return new ChatResponse(
                ragChatService.ask(request.question())
        );
    }

}