package org.kgromov.drugstore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    @Value("classpath:prompts/drug-info2.st")
    private Resource drugInfoPrompt;

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultUser(drugInfoPrompt)
                .build();
    }
}
