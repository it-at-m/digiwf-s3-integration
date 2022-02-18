package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3AndDatabaseCleanupService {

    private final S3Repository s3Repository;

    private final FolderRepository folderRepository;

    private final FolderHandlingService folderHandlingService;

    /**
     * Cronjob scheduled method which deletes all folders in the S3 storage and database
     * for which the {@link Folder#getEndOfLife()} attribute is exceeded.
     */
    public void cleanUpExpiredFolders() {
        log.info("S3 and database clean up for expired folders started.");
        this.folderRepository.findAllByEndOfLifeNotNullAndEndOfLifeBefore(LocalDate.now())
                .forEach(this::deleteFolder);
        log.info("S3 and database clean up for expired folders  finished.");
    }

    private void deleteFolder(final Folder folder) {
        try {
            this.folderHandlingService.deleteFolder(folder.getRefId());
        } catch (final Exception exception) {
            log.error("Error during cleanup happened.", exception);
        }
    }

    /**
     * Cronjob scheduled method that deletes all {@link Folder} entities in the database
     * for which no corresponding folder exists in the S3 storage.
     */
    public void cleanUpDatabaseFolderWithoutCorrespondingS3Folder() {
        log.info("Database clean up for folder without corresponding S3 folders started.");
        this.folderRepository.findAllByEndOfLifeIsNull()
                .filter(this::shouldDatabaseFolderBeDeleted)
                .forEach(this::deleteDatabaseFolder);
        log.info("Database clean up for folder without corresponding S3 folders finished.");
    }

    /**
     * Checks whether the folder should be deleted from the database.
     *
     * @param folder to check
     * @return true if the folder is to be deleted from the database. Otherwise false.
     */
    public boolean shouldDatabaseFolderBeDeleted(final Folder folder) {
        final boolean deleteDatabaseFolder = false;
        try {
            return this.s3Repository.getFilepathesFromFolder(folder.getRefId()).isEmpty();
        } catch (final Exception exception) {
            log.error("Error during cleanup happened.", exception);
        }
        return deleteDatabaseFolder;
    }

    @Transactional
    public void deleteDatabaseFolder(final Folder folder) {
        this.folderRepository.deleteById(folder.getId());
    }

}
