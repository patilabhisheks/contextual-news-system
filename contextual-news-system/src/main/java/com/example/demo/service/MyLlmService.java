package com.example.demo.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyLlmService {
    private final OpenAiService openAiService;

    public MyLlmService() {
        String apiKey = System.getenv("OPENAI_API_KEY");
        this.openAiService = new OpenAiService(apiKey);
    }

    public String summarise(String title, String description) {
        String prompt = "Summarise in one sentence:\n" + title + "\n" + description;

        ChatMessage systemMessage = new ChatMessage("system", "You are a helpful summariser.");
        ChatMessage userMessage   = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(systemMessage, userMessage))
                .maxTokens(60)
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent();
    }

}
