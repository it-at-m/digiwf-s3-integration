package io.muenchendigital.digiwf.s3.integration.domain.service.cronjob;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.File;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FileRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CleanUpDatabaseFilesWithoutCorrespondingS3FolderTest {

    @Mock
    private S3Repository s3Repository;

    @Mock
    private FileRepository fileRepository;

    private CleanUpDatabaseFilesWithoutCorrespondingS3Folder cleanUpDatabaseFolderWithoutCorrespondingS3Folder;

    @BeforeEach
    public void beforeEach() {
        this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder = new CleanUpDatabaseFilesWithoutCorrespondingS3Folder(this.s3Repository, this.fileRepository);
    }

    @Test
    void shouldDatabaseFolderBeDeleted() throws S3AccessException {
        final File file = new File();
        file.setPathToFile("folder/file.txt");

        // Creation date is more than one month ago.
        file.setCreatedTime(LocalDateTime.now().minusMonths(1).minusDays(1));
        Mockito.when(this.s3Repository.isFileExisting(file.getPathToFile()))
                .thenReturn(Boolean.TRUE);
        assertThat(this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.shouldDatabaseFileBeDeleted(file), is(false));

        Mockito.when(this.s3Repository.isFileExisting(file.getPathToFile()))
                .thenReturn(Boolean.FALSE);
        assertThat(this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.shouldDatabaseFileBeDeleted(file), is(true));

        // Creation date is exactly one month or less ago.
        file.setCreatedTime(LocalDateTime.now().minusMonths(1));
        Mockito.when(this.s3Repository.isFileExisting(file.getPathToFile()))
                .thenReturn(Boolean.TRUE);
        assertThat(this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.shouldDatabaseFileBeDeleted(file), is(false));

        Mockito.when(this.s3Repository.isFileExisting(file.getPathToFile()))
                .thenReturn(Boolean.FALSE);
        assertThat(this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.shouldDatabaseFileBeDeleted(file), is(false));
    }


}