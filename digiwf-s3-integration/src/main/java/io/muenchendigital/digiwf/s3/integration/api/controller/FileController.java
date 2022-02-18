package io.muenchendigital.digiwf.s3.integration.api.controller;

import io.muenchendigital.digiwf.s3.integration.api.dto.FileDataDto;
import io.muenchendigital.digiwf.s3.integration.api.dto.FileResponseDto;
import io.muenchendigital.digiwf.s3.integration.api.mapper.FileDataMapper;
import io.muenchendigital.digiwf.s3.integration.api.mapper.FileResponseMapper;
import io.muenchendigital.digiwf.s3.integration.domain.exception.FileExistanceException;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileResponse;
import io.muenchendigital.digiwf.s3.integration.domain.service.FileHandlingService;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileHandlingService fileHandlingService;

    private final FileDataMapper fileMapper;

    private final FileResponseMapper fileResponseMapper;

    @GetMapping(value = "/{refId}")
    public ResponseEntity<FileResponseDto> get(@PathVariable @NotEmpty @Size(max = FolderRepository.LENGTH_REF_ID) final String refId,
                                               @RequestParam @NotEmpty final String fileName,
                                               @RequestParam @NotNull @Min(FileHandlingService.MIN_EXPIRES_IN_MINUTES) final Integer expiresInMinutes) {
        try {
            log.info("Received a request for S3 presigned url to download a file");
            final FileResponse fileResponse = this.fileHandlingService.getFile(refId, fileName, expiresInMinutes);
            final FileResponseDto fileResponseDto = this.fileResponseMapper.model2Dto(fileResponse);
            return ResponseEntity.ok(fileResponseDto);
        } catch (final S3AccessException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<FileResponseDto> save(@RequestBody @NotNull @Valid final FileDataDto fileData) {
        try {
            log.info("Received a request for S3 presigned url to upload a new file");
            final FileResponse fileResponse = this.fileHandlingService.saveFile(this.fileMapper.dto2Model(fileData));
            final FileResponseDto fileResponseDto = this.fileResponseMapper.model2Dto(fileResponse);
            return ResponseEntity.ok(fileResponseDto);
        } catch (final FileExistanceException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<FileResponseDto> update(@RequestBody @NotNull @Valid final FileDataDto fileData) {
        try {
            log.info("Received a request for S3 presigned url to upload a existing file");
            final FileResponse fileResponse = this.fileHandlingService.updateFile(this.fileMapper.dto2Model(fileData));
            final FileResponseDto fileResponseDto = this.fileResponseMapper.model2Dto(fileResponse);
            return ResponseEntity.ok(fileResponseDto);
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @DeleteMapping(value = "/{refId}")
    public ResponseEntity<FileResponseDto> delete(@PathVariable @NotEmpty @Size(max = FolderRepository.LENGTH_REF_ID) final String refId,
                                                  @RequestParam @NotEmpty final String fileName,
                                                  @RequestParam @NotNull @Min(FileHandlingService.MIN_EXPIRES_IN_MINUTES) final Integer expiresInMinutes) {
        try {
            log.info("Received a request for S3 presigned url to delete a file");
            final FileResponse fileResponse = this.fileHandlingService.deleteFile(refId, fileName, expiresInMinutes);
            final FileResponseDto fileResponseDto = this.fileResponseMapper.model2Dto(fileResponse);
            return ResponseEntity.ok(fileResponseDto);
        } catch (final S3AccessException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

}
