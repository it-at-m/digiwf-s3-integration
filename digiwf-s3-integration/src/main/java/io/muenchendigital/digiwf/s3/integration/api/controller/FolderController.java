package io.muenchendigital.digiwf.s3.integration.api.controller;

import io.muenchendigital.digiwf.s3.integration.domain.service.FolderHandlingService;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
public class FolderController {

    private final FolderHandlingService folderHandlingService;

    @DeleteMapping(value = "/{refId}")
    public ResponseEntity<Void> delete(@PathVariable @NotEmpty @Size(max = FolderRepository.LENGTH_REF_ID) final String refId) {
        try {
            this.folderHandlingService.deleteFolder(refId);
            return ResponseEntity.noContent().build();
        } catch (final Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

}
