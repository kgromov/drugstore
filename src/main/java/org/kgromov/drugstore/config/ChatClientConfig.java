package org.kgromov.drugstore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

import static com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

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

//    @Primary
//    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        return objectMapper;
    }
}
