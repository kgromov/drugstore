package org.kgromov.drugstore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.kgromov.drugstore.model.DrugsImageResponse;
import org.kgromov.drugstore.model.DrugsInfo;
import org.kgromov.drugstore.repository.DrugsRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DrugsImageService {
    private final ChatClient chatClient;
    private final DrugsRepository drugsRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void processImage(Resource imageResource) {
        try {
//            File image = ImageUtils.resizeImage(imageResource.getInputStream(), imageResource.getFilename(), 500, 500);
            String md5 = ImageUtils.m5Digest(imageResource.getInputStream());
            if (drugsRepository.hasDrugsByDigest(md5)) {
                log.info("Image already processed");
                return;
            }
//            DrugsInfo drugsInfo = this.convertToMode(new FileSystemResource(image));
//            Resource resource = new ByteArrayResource((imageResource.getContentAsByteArray()));
            var response = this.convertToModel(imageResource);
            var drugsInfo = DrugsInfo.builder()
                    .name(response.name())
                    .category(response.category())
                    .form(response.form())
                    .expirationDate(response.expirationDate())
                    .md5(md5)
                    .build();
            drugsRepository.save(drugsInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DrugsImageResponse convertToModel(Resource image) {
        var parser = new BeanOutputConverter<>(DrugsImageResponse.class, objectMapper);
        return chatClient
                .prompt()
                .user(um -> um.media(MimeTypeUtils.IMAGE_PNG, image))
                .call()
                .entity(parser);
    }
}
