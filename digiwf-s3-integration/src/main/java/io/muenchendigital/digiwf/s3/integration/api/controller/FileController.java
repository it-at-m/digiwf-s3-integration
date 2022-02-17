package io.muenchendigital.digiwf.s3.integration.api.controller;

import io.muenchendigital.digiwf.s3.integration.api.dto.FileDataDto;
import io.muenchendigital.digiwf.s3.integration.api.mapper.FileDataMapper;
import io.muenchendigital.digiwf.s3.integration.domain.exception.FileExistanceException;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileResource;
import io.muenchendigital.digiwf.s3.integration.domain.service.FileHandlingService;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileHandlingService fileHandlingService;

    private final FileDataMapper fileMapper;

    @GetMapping(value = "/{refId}")
    public ResponseEntity<Resource> get(@PathVariable @NotEmpty @Size(max = FolderRepository.LENGTH_REF_ID) final String refId,
                                        @RequestParam @NotEmpty final String fileName) {
        try {
            final FileResource fileResource = this.fileHandlingService.getFile(refId, fileName);
            // Erstellen des MediaType
            final MediaType mediaType = MediaTypeFactory
                    .getMediaType(fileResource.getResource())
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);
            // Ermöglichen des Dateidownloads bei Aufruf über Browser
            final ContentDisposition disposition = ContentDisposition
                    .attachment()
                    .filename(fileResource.getFilename())
                    .build();
            // Setzen der Header
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDisposition(disposition);
            // Erstellen der Response
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileResource.getResource());
        } catch (final S3AccessException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> save(@ModelAttribute @NotNull @Valid final FileDataDto file) {
        try {
            this.fileHandlingService.saveFile(this.fileMapper.dto2Model(file));
            return ResponseEntity.ok().build();
        } catch (final S3AccessException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (final FileExistanceException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> update(@ModelAttribute @NotNull @Valid final FileDataDto file) {
        try {
            this.fileHandlingService.updateFile(this.fileMapper.dto2Model(file));
            return ResponseEntity.ok().build();
        } catch (final S3AccessException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @DeleteMapping(value = "/{refId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable @NotEmpty @Size(max = FolderRepository.LENGTH_REF_ID) final String refId,
                                       @RequestParam @NotEmpty final String fileName) {
        try {
            this.fileHandlingService.deleteFile(refId, fileName);
            return ResponseEntity.noContent().build();
        } catch (final S3AccessException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

}
