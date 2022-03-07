package io.muenchendigital.digiwf.s3.integration.client.controller;

import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.repository.DocumentStorageFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class ClientFileUsageController {

    private static final String filename = "cat.jpg";

    private final DocumentStorageFileRepository documentStorageFileRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void getFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException, IOException {
        final byte[] binaryFile = this.documentStorageFileRepository.getFile(
                ClientFolderUsageController.FOLDER,
                filename,
                3
        );
        final File tmpFile = File.createTempFile("test", ".jpg");
        Files.write(tmpFile.toPath(), binaryFile);
        log.info("File downloaded to {}.", tmpFile.toPath());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void saveFile() throws IOException, DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final File file = ResourceUtils.getFile("classpath:files/cat.jpg");
        final byte[] binaryFile = Files.readAllBytes(file.toPath());
        this.documentStorageFileRepository.saveFile(
                ClientFolderUsageController.FOLDER,
                filename,
                binaryFile,
                3,
                LocalDate.now().plusMonths(1)
        );
        log.info("File saved.");
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateFile() throws IOException, DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final File file = ResourceUtils.getFile("classpath:files/sunflower.jpg");
        final byte[] binaryFile = Files.readAllBytes(file.toPath());
        // Overwrite file on S3 with sunflower.jpg
        this.documentStorageFileRepository.updateFile(
                ClientFolderUsageController.FOLDER,
                filename,
                binaryFile,
                3,
                LocalDate.now().plusMonths(1)
        );
        log.info("File updated.");
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        this.documentStorageFileRepository.deleteFile(
                ClientFolderUsageController.FOLDER,
                filename,
                3
        );
        log.info("File deleted.");
    }

}
