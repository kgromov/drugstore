package org.kgromov.drugstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kgromov.drugstore.model.DrugImageDigest;
import org.kgromov.drugstore.model.DrugsInfo;
import org.kgromov.drugstore.repository.DrugsDigestRepository;
import org.kgromov.drugstore.repository.DrugsRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DrugsImageService {
    private final ChatClient chatClient;
    private final DrugsRepository drugsRepository;
    private final DrugsDigestRepository digestRepository;

    @Transactional
    public void processImage(MultipartFile file) {
        try {
//            File image = ImageUtils.resizeImage(file.getInputStream(), file.getName(), 500, 500);
            String md5 = ImageUtils.m5Digest(file.getInputStream());
            if (digestRepository.hasDigestByDigest(md5)) {
                log.info("Image already processed");
                return;
            }
//            DrugsInfo drugsInfo = describeImage(new FileSystemResource(image));
            DrugsInfo drugsInfo = describeImage(file.getResource());
            drugsRepository.save(drugsInfo);
            digestRepository.save(new DrugImageDigest(drugsInfo, md5));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DrugsInfo describeImage(Resource image) {
        return chatClient
                .prompt()
                .user(um -> um.media(MimeTypeUtils.IMAGE_PNG, image))
                .call()
                .entity(DrugsInfo.class);
    }
}
