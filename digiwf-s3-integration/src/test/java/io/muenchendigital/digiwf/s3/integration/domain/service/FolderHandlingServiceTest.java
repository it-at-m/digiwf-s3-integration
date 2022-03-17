package io.muenchendigital.digiwf.s3.integration.domain.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FolderHandlingServiceTest {
/*
    @Mock
    private S3Repository s3Repository;

    @Mock
    private FileRepository fileRepository;

    private FolderHandlingService folderHandlingService;

    @BeforeEach
    public void beforeEach() {
        this.folderHandlingService = new FolderHandlingService(this.s3Repository, this.fileRepository);
    }

    @Test
    void deleteFolderException() throws S3AccessException {
        final UUID uuid = UUID.randomUUID();
        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>());
        Mockito.when(this.fileRepository.findByPathToFile(uuid.toString())).thenReturn(Optional.of(new File()));
        Assertions.assertThrows(S3AndDatabaseAsyncException.class, () -> this.folderHandlingService.deleteFolder(uuid.toString()));

        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>(List.of("test.txt")));
        Mockito.when(this.fileRepository.findByPathToFile(uuid.toString())).thenReturn(Optional.empty());
        Assertions.assertThrows(S3AndDatabaseAsyncException.class, () -> this.folderHandlingService.deleteFolder(uuid.toString()));

        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>());
        Mockito.when(this.fileRepository.findByPathToFile(uuid.toString())).thenReturn(Optional.empty());
        Assertions.assertDoesNotThrow(() -> this.folderHandlingService.deleteFolder(uuid.toString()));
    }

    @Test
    void deleteFolder() throws S3AccessException {
        final UUID uuid = UUID.randomUUID();
        final String filePath = uuid.toString().concat("/test.txt");
        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>(List.of(filePath)));
        Mockito.when(this.fileRepository.findByPathToFile(uuid.toString())).thenReturn(Optional.of(new File()));
        Assertions.assertDoesNotThrow(() -> this.folderHandlingService.deleteFolder(uuid.toString()));
    }


    @Test
    void updateEndOfLife() throws FolderExistanceException {
        final String refId = UUID.randomUUID().toString();
        final LocalDate newEndOfLife = LocalDate.now().plusYears(1);
        final File folder = new File();
        Mockito.when(this.fileRepository.findByPathToFile(refId)).thenReturn(Optional.of(folder));
        this.folderHandlingService.updateEndOfLife(refId, newEndOfLife);

        assertThat(newEndOfLife, is(folder.getEndOfLife()));
        Mockito.verify(this.fileRepository, Mockito.times(1)).save(folder);
    }
*/
}
