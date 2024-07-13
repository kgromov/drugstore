package org.kgromov.drugstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageAiService {
    private final ChatClient chatClient;


}