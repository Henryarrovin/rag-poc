package com.henry.rag.rag.retrieval;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagChatService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String ask(String question) {
        var advisor = QuestionAnswerAdvisor
                .builder(vectorStore)
                .searchRequest(
                        SearchRequest.builder()
                                .topK(5)
                                .similarityThreshold(0.7)
                                .build()
                )
                .build();

        return chatClient.prompt()
                .advisors(advisor)
                .system("""
                    You are a financial assistant.
                    Answer ONLY from the provided context.
                    If the answer is not found, say you don't know.
                """)
                .user(question)
                .call()
                .content();
    }

}
