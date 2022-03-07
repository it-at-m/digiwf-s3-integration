package io.muenchendigital.digiwf.s3.integration.api.controller;

import io.muenchendigital.digiwf.s3.integration.domain.exception.FolderExistanceException;
import io.muenchendigital.digiwf.s3.integration.domain.service.FolderHandlingService;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "FolderAPI", description = "API to interact with folders")
@RequestMapping("/folder")
public class FolderController {

    private final FolderHandlingService folderHandlingService;

    @DeleteMapping(value = "/{refId}")
    @Operation(description = "Deletes the folder specified in the parameter together with the corresponding database entry")
    public ResponseEntity<Void> delete(@PathVariable @NotEmpty @Size(max = FolderRepository.LENGTH_REF_ID) final String refId) {
        try {
            log.info("Received a request for deletion of a certain folder.");
            this.folderHandlingService.deleteFolder(refId);
            return ResponseEntity.noContent().build();
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @PutMapping(value = "/{refId}")
    @Operation(description = "Updates the end of life attribute in the corresponding database entry for the folder specified in the parameter")
    public ResponseEntity<Void> updateEndOfLife(@PathVariable @NotEmpty @Size(max = FolderRepository.LENGTH_REF_ID) final String refId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endOfLife) {
        try {
            log.info("Received a request for updating the end of life of a certain folder.");
            this.folderHandlingService.updateEndOfLife(refId, endOfLife);
            return ResponseEntity.ok().build();
        } catch (final FolderExistanceException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
