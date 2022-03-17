package io.muenchendigital.digiwf.s3.integration.domain.service.cronjob;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.File;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FileRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanUpDatabaseFilesWithoutCorrespondingS3Folder {

    private final S3Repository s3Repository;

    private final FileRepository fileRepository;

    /**
     * Cronjob scheduled method that deletes all {@link File} entities in the database
     * for which no corresponding file exists in the S3 storage.
     * <p>
     * The deletion is performed only if the file entity was created more than a month ago.
     */
    public void cleanUp() {
        log.info("Database clean up for folder without corresponding S3 folders started.");
        this.fileRepository.findAllByEndOfLifeIsNull()
                .filter(this::shouldDatabaseFileBeDeleted)
                .forEach(this::deleteFileInDatabase);
        log.info("Database clean up for folder without corresponding S3 folders finished.");
    }

    /**
     * Checks whether the file should be deleted from database.
     *
     * @param file to check
     * @return true if the file has to be deleted from the database. Otherwise false.
     */
    public boolean shouldDatabaseFileBeDeleted(final File file) {
        boolean deleteDatabaseFile = false;
        try {
            final boolean noFileExistsInS3 = !this.s3Repository.isFileExisting(file.getPathToFile());
            final LocalDate creationDate = file.getCreatedTime().toLocalDate();
            final boolean folderCreatedMoreThanAMonthAgo = creationDate.isBefore(LocalDate.now().minusMonths(1));
            deleteDatabaseFile = noFileExistsInS3 && folderCreatedMoreThanAMonthAgo;
        } catch (final NullPointerException exception) {
            log.error("Created time in file entity not set.", exception);
        } catch (final S3AccessException exception) {
            log.error("S3 storage could not be accessed.", exception);
        } catch (final Exception exception) {
            log.error("Error during cleanup happened.", exception);
        }
        return deleteDatabaseFile;
    }

    @Transactional
    public void deleteFileInDatabase(final File file) {
        this.fileRepository.deleteById(file.getId());
    }

}
