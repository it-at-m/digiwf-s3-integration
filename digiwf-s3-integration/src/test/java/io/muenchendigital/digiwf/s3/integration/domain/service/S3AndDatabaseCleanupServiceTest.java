package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AndDatabaseAsyncException;
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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class S3AndDatabaseCleanupServiceTest {

    @Mock
    private S3Repository s3Repository;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private FolderHandlingService folderHandlingService;

    private S3AndDatabaseCleanupService s3AndDatabaseCleanupService;

    @BeforeEach
    public void beforeEach() {
        this.s3AndDatabaseCleanupService = new S3AndDatabaseCleanupService(this.s3Repository, this.folderRepository, this.folderHandlingService);
    }

    @Test
    void cleanUp() throws S3AccessException, S3AndDatabaseAsyncException {
        final var folder1 = new Folder();
        folder1.setRefId("folder1");
        folder1.setEndOfLife(LocalDate.now().minusYears(1));
        final var folder2 = new Folder();
        folder2.setRefId("folder2");
        folder2.setEndOfLife(LocalDate.now().minusYears(1));
        final var folder3 = new Folder();
        folder3.setRefId("folder3");
        folder3.setEndOfLife(LocalDate.now().minusYears(1));
        final Stream<Folder> folderStream = Stream.of(folder1, folder2, folder3);

        Mockito.when(this.folderRepository.findAllByEndOfLifeNotNullAndEndOfLifeBefore(Mockito.any(LocalDate.class)))
                .thenReturn(folderStream);
        this.s3AndDatabaseCleanupService.cleanUpExpiredFolders();
        Mockito.verify(this.folderHandlingService, Mockito.times(3)).deleteFolder(Mockito.anyString());

    }

    @Test
    void shouldDatabaseFolderBeDeleted() throws S3AccessException {
        final Folder folder = new Folder();
        folder.setRefId("folder");

        Mockito.when(this.s3Repository.getFilepathesFromFolder(folder.getRefId()))
                .thenReturn(new HashSet<>(List.of("folder/the-file.txt")));
        assertThat(this.s3AndDatabaseCleanupService.shouldDatabaseFolderBeDeleted(folder), is(false));

        Mockito.when(this.s3Repository.getFilepathesFromFolder(folder.getRefId()))
                .thenReturn(new HashSet<>());
        assertThat(this.s3AndDatabaseCleanupService.shouldDatabaseFolderBeDeleted(folder), is(true));
    }

}
