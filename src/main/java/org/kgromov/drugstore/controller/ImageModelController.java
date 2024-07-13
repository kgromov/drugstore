package org.kgromov.drugstore.controller;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.service.ImageUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageModelController {
    private final ChatModel chatModel;
    // init rather ChatClient with default (or at least system) prompts?

    @GetMapping("/describe")
    public String describe() {
        var imageResource = new ClassPathResource("/images/drugs.jpg");
        UserMessage userMessage = new UserMessage("""
                The following is a screenshot of drugs.
                Can you do your best to provide a name and expiration date?
                Answer in format: name - date(MM/yyyy)
                """,
                List.of(new Media(MimeTypeUtils.IMAGE_JPEG, imageResource)));
        var response = chatModel.call(new Prompt(userMessage));
        return response.getResult().getOutput().getContent();
    }

    @PostMapping(value = "/upload-image",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        File image = ImageUtils.resizeImage(file.getInputStream(), file.getName(), 500, 500);
        FileSystemResource resource = new FileSystemResource(image);
        return  ChatClient.builder(chatModel)
                .build()
                .prompt()
                .system("""
                         The following is a screenshot of drugs.
                         Can you do your best to provide a name and expiration date?
                         Answer in format: name - date(MM/yyyy)
                         """)
                .user(um -> um.media(MimeTypeUtils.IMAGE_PNG, file.getResource()))
                .call()
                .content();
    }
}
