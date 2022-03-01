package io.muenchendigital.digiwf.s3.integration.domain.service.cronjob;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanUpDatabaseFolderWithoutCorrespondingS3Folder {

    private final S3Repository s3Repository;

    private final FolderRepository folderRepository;

    /**
     * Cronjob scheduled method that deletes all {@link Folder} entities in the database
     * for which no corresponding folder exists in the S3 storage.
     * <p>
     * The deletion is performed only if the folder entity was created more than a month ago.
     */
    public void cleanUp() {
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
     * @return true if the folder has to be deleted from the database. Otherwise false.
     */
    public boolean shouldDatabaseFolderBeDeleted(final Folder folder) {
        boolean deleteDatabaseFolder = false;
        try {
            final boolean noFilesInS3Folder = this.s3Repository.getFilepathesFromFolder(folder.getRefId()).isEmpty();
            final LocalDate creationDate = folder.getCreatedTime().toLocalDate();
            final boolean folderCreatedMoreThanAMonthAgo = creationDate.isBefore(LocalDate.now().minusMonths(1));
            deleteDatabaseFolder = noFilesInS3Folder && folderCreatedMoreThanAMonthAgo;
        } catch (final NullPointerException exception) {
            log.error("Created time in folder entity not set.", exception);
        } catch (final S3AccessException exception) {
            log.error("S3 storage could not be accessed.", exception);
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
