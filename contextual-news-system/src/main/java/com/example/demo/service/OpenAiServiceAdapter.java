package com.example.demo.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.stereotype.Component;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;

@Component
public class OpenAiServiceAdapter {

    private final OpenAiService client = new OpenAiService(
            System.getenv("OPENAI_API_KEY"));

    public String summarise(String title, String desc) {
        String prompt = "Summarise in one sentence:\n" + title + "\n" + desc;

        ChatMessage systemMessage = new ChatMessage(
                "system", "You are a helpful summariser.");
        ChatMessage userMessage   = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(systemMessage, userMessage))
                .maxTokens(60)
                .build();

        ChatCompletionResult result = client.createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent().trim();
    }

    public ParsedQuery parse(String q){
        q = q.toLowerCase();
        if (q.contains("near") || q.contains("nearby")) return new ParsedQuery("nearby", q);
        if (q.contains("technology") || q.contains("business") || q.contains("sports"))
            return new ParsedQuery("category", q);
        if (q.contains("score")) return new ParsedQuery("score", q);
        if (q.contains("from"))   return new ParsedQuery("source", q);
        return new ParsedQuery("search", q);
    }

    public record ParsedQuery(String intent, String term){}
}
