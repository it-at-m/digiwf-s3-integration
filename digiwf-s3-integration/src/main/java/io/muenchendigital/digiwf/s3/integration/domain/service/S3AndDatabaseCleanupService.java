package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3AndDatabaseCleanupService {

    private final FolderRepository folderRepository;

    private final FolderHandlingService folderHandlingService;

    /**
     * Durch Cronjob terminierte Methode welche alle Ordner im S3-Storage und in der Datenbank löscht,
     * für die das Attribut {@link Folder#getEndOfLife()} überschritten ist.
     */
    public void cleanUp() {
        log.info("S3 and database clean up started.");
        this.folderRepository.findAllByEndOfLifeNotNullAndEndOfLifeBefore(LocalDate.now())
                .forEach(this::deleteFolder);
        log.info("S3 and database clean up finished.");
    }

    private void deleteFolder(final Folder folder) {
        try {
            this.folderHandlingService.deleteFolder(folder.getRefId());
        } catch (final Exception exception) {
            log.error("Error during cleanup happened.", exception);
        }
    }

}
