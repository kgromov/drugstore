package org.kgromov.drugstore.ui.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kgromov.drugstore.service.DrugsImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.InputStream;

@SpringComponent
@Slf4j
@UIScope
@RequiredArgsConstructor
public class DrugsUploader extends VerticalLayout {
    private final DrugsImageService drugsImageService;
    private final Sinks.Many<Boolean> imageRecognitionSubject = Sinks.many().multicast().directBestEffort();
    private final MultiFileMemoryBuffer memoryBuffer = new MultiFileMemoryBuffer();
    private final Upload fileUploader = new Upload(memoryBuffer);
    private Span errorField;
    private final Button uploadAllButton = new Button("Upload", VaadinIcon.ARROW_UP.create());

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        this.configureFileUploader();
    }

    public Flux<Boolean> emitter() {
        return this.imageRecognitionSubject.asFlux();
    }

    private void configureFileUploader() {
        fileUploader.setMaxFileSize(5 * 1024 * 1024);
        fileUploader.setAcceptedFileTypes(
                MimeTypeUtils.IMAGE_PNG_VALUE,
                MimeTypeUtils.IMAGE_JPEG_VALUE
        );
        fileUploader.setAutoUpload(false);
        fileUploader.setMaxFiles(10);
        fileUploader.setDropLabel(new Span("Drop pdf files to merge"));
       /* errorField = new Span();
        errorField.setVisible(false);
        errorField.getStyle().set("color", "red");*/
        this.configureUpload();

        fileUploader.addSucceededListener(event -> {
            String fileName = event.getFileName();
            try (InputStream fileData = memoryBuffer.getInputStream(fileName)) {
                long contentLength = event.getContentLength();
                log.info("File {} uploaded: length = {} KB", fileName, contentLength / 1024);
                drugsImageService.processImage(new InputStreamResource(fileData));
                this.imageRecognitionSubject.tryEmitNext(true);
                this.showSuccessNotification("File " + fileName + " uploaded");
            } catch (Exception e) {
                this.imageRecognitionSubject.tryEmitNext(false);
                throw new RuntimeException(e);
            }
        });
        fileUploader.addFileRejectedListener(event -> this.showErrorNotification(event.getErrorMessage()));
        fileUploader.addFailedListener(event -> this.showErrorNotification(event.getReason().getMessage()));
    }

    private void configureUpload() {
        UploadI18N i18n = new UploadI18N();
        i18n.setAddFiles(new UploadI18N.AddFiles());
        i18n.getAddFiles().setMany("Select Files...");
        fileUploader.setI18n(i18n);
        uploadAllButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        uploadAllButton.addClickListener(event -> {
            // No explicit Flow API for this at the moment
            fileUploader.getElement().callJsFunction("uploadFiles");
        });
        add(uploadAllButton);
    }

    private void showSuccessNotification(String message) {
        var notification = Notification.show(message,
                1000,
                Notification.Position.BOTTOM_END
        );
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showErrorNotification(String message) {
        var notification = Notification.show(message,
                2000,
                Notification.Position.MIDDLE
        );
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
