package org.kgromov.drugstore.controller;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.service.DrugsImageService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class DrugsImageController {
    private final DrugsImageService drugsImageService;

    @GetMapping("/test")
    public void processSample(@RequestParam String image) {
        var imageResource = new ClassPathResource("/images/%s".formatted(image));
        drugsImageService.processImage(imageResource);
    }

    @PostMapping(value = "/upload-image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void processUpload(@RequestParam("file") MultipartFile file) {
        drugsImageService.processImage(file.getResource());
    }
}
