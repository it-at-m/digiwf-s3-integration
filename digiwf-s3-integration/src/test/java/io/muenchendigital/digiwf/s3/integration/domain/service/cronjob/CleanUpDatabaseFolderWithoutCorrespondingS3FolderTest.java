package io.muenchendigital.digiwf.s3.integration.domain.service.cronjob;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
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
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CleanUpDatabaseFolderWithoutCorrespondingS3FolderTest {

    @Mock
    private S3Repository s3Repository;

    @Mock
    private FolderRepository folderRepository;

    private CleanUpDatabaseFolderWithoutCorrespondingS3Folder cleanUpDatabaseFolderWithoutCorrespondingS3Folder;

    @BeforeEach
    public void beforeEach() {
        this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder = new CleanUpDatabaseFolderWithoutCorrespondingS3Folder(this.s3Repository, this.folderRepository);
    }

    @Test
    void shouldDatabaseFolderBeDeleted() throws S3AccessException {
        final Folder folder = new Folder();
        folder.setRefId("folder");

        // Creation date is more than one month ago.
        folder.setCreatedTime(LocalDateTime.now().minusMonths(1).minusDays(1));
        Mockito.when(this.s3Repository.getFilepathesFromFolder(folder.getRefId()))
                .thenReturn(new HashSet<>(List.of("folder/the-file.txt")));
        assertThat(this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.shouldDatabaseFolderBeDeleted(folder), is(false));

        Mockito.when(this.s3Repository.getFilepathesFromFolder(folder.getRefId()))
                .thenReturn(new HashSet<>());
        assertThat(this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.shouldDatabaseFolderBeDeleted(folder), is(true));

        // Creation date is exactly one month or less ago.
        folder.setCreatedTime(LocalDateTime.now().minusMonths(1));
        Mockito.when(this.s3Repository.getFilepathesFromFolder(folder.getRefId()))
                .thenReturn(new HashSet<>(List.of("folder/the-file.txt")));
        assertThat(this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.shouldDatabaseFolderBeDeleted(folder), is(false));

        Mockito.when(this.s3Repository.getFilepathesFromFolder(folder.getRefId()))
                .thenReturn(new HashSet<>());
        assertThat(this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.shouldDatabaseFolderBeDeleted(folder), is(false));
    }


}