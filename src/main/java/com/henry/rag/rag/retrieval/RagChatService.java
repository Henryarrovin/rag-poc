package com.henry.rag.rag.retrieval;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagChatService {

    private final ChatClient chatClient;
    public RagChatService(ChatClient.Builder builder, VectorStore vectorStore) {
        QuestionAnswerAdvisor advisor =
                QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(
                                SearchRequest.builder()
                                        .topK(5)
                                        .similarityThreshold(0.7)
                                        .build()
                        )
                        .build();

        this.chatClient = builder
                .defaultAdvisors(advisor)
                .build();
    }

    public String ask(String question) {
        return chatClient.prompt()
                .system("""
                        You are a financial analysis assistant.
                        Answer only from the provided context.
                        If the answer is not present, say you don't know.
                        """)
                .user(question)
                .call()
                .content();
    }

}