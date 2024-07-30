package org.kgromov.drugstore.controller;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.service.DrugsImageService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DrugsImageController {
    private final DrugsImageService drugsImageService;

    @GetMapping("/test")
    public void createSample(@RequestParam String image) {
        var imageResource = new ClassPathResource("/images/%s".formatted(image));
        drugsImageService.processImage(imageResource);
    }

    @PostMapping(value = "/upload-image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void upload(@RequestParam("file") MultipartFile file) {
        drugsImageService.processImage(file.getResource());
    }
}
