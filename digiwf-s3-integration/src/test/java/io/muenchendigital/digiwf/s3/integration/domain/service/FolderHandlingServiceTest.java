package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AndDatabaseAsyncException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FolderHandlingServiceTest {

    @Mock
    private S3Repository s3Repository;

    @Mock
    private FolderRepository folderRepository;

    private FolderHandlingService folderHandlingService;

    @BeforeEach
    public void beforeEach() {
        this.folderHandlingService = new FolderHandlingService(this.s3Repository, this.folderRepository);
    }

    @Test
    void deleteFolderException() throws S3AccessException {
        final UUID uuid = UUID.randomUUID();
        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>());
        Mockito.when(this.folderRepository.findByRefId(uuid.toString())).thenReturn(Optional.of(new Folder()));
        Assertions.assertThrows(S3AndDatabaseAsyncException.class, () -> this.folderHandlingService.deleteFolder(uuid.toString()));

        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>(List.of("test.txt")));
        Mockito.when(this.folderRepository.findByRefId(uuid.toString())).thenReturn(Optional.empty());
        Assertions.assertThrows(S3AndDatabaseAsyncException.class, () -> this.folderHandlingService.deleteFolder(uuid.toString()));

        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>());
        Mockito.when(this.folderRepository.findByRefId(uuid.toString())).thenReturn(Optional.empty());
        Assertions.assertDoesNotThrow(() -> this.folderHandlingService.deleteFolder(uuid.toString()));
    }

    @Test
    void deleteFolder() throws S3AccessException {
        final UUID uuid = UUID.randomUUID();
        final String filePath = uuid.toString().concat("/test.txt");
        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>(List.of(filePath)));
        Mockito.when(this.folderRepository.findByRefId(uuid.toString())).thenReturn(Optional.of(new Folder()));
        Assertions.assertDoesNotThrow(() -> this.folderHandlingService.deleteFolder(uuid.toString()));
    }

}
