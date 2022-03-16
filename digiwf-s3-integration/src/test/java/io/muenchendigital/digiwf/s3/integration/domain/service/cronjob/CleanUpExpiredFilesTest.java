package io.muenchendigital.digiwf.s3.integration.domain.service.cronjob;

import io.muenchendigital.digiwf.s3.integration.domain.service.FolderHandlingService;
import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.File;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AndDatabaseAsyncException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CleanUpExpiredFilesTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FolderHandlingService folderHandlingService;

    private CleanUpExpiredFiles cleanUpExpiredFiles;

    @BeforeEach
    public void beforeEach() {
        this.cleanUpExpiredFiles = new CleanUpExpiredFiles(this.fileRepository, this.folderHandlingService);
    }

    @Test
    void cleanUp() throws S3AccessException, S3AndDatabaseAsyncException {
        final var folder1 = new File();
        folder1.setRefId("folder1");
        folder1.setEndOfLife(LocalDate.now().minusYears(1));
        final var folder2 = new File();
        folder2.setRefId("folder2");
        folder2.setEndOfLife(LocalDate.now().minusYears(1));
        final var folder3 = new File();
        folder3.setRefId("folder3");
        folder3.setEndOfLife(LocalDate.now().minusYears(1));
        final Stream<File> folderStream = Stream.of(folder1, folder2, folder3);

        Mockito.when(this.fileRepository.findAllByEndOfLifeNotNullAndEndOfLifeBefore(Mockito.any(LocalDate.class)))
                .thenReturn(folderStream);
        this.cleanUpExpiredFiles.cleanUp();
        Mockito.verify(this.folderHandlingService, Mockito.times(3)).deleteFolder(Mockito.anyString());
    }

}
