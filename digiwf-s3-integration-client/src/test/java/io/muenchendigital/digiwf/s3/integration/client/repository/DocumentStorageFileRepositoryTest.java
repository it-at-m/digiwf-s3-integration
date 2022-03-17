package io.muenchendigital.digiwf.s3.integration.client.repository;

import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.repository.presignedurl.PresignedUrlRepository;
import io.muenchendigital.digiwf.s3.integration.client.repository.transfer.S3FileTransferRepository;
import io.muenchendigital.digiwf.s3.integration.gen.api.FileApiApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class DocumentStorageFileRepositoryTest {

    @Mock
    private PresignedUrlRepository presignedUrlRepository;

    @Mock
    private S3FileTransferRepository s3FileTransferRepository;

    @Mock
    private FileApiApi fileApiApi;

    private DocumentStorageFileRepository documentStorageFileRepository;

    @BeforeEach
    public void beforeEach() {
        this.documentStorageFileRepository = new DocumentStorageFileRepository(this.presignedUrlRepository, this.s3FileTransferRepository, this.fileApiApi);
        Mockito.reset(this.presignedUrlRepository, this.s3FileTransferRepository, this.fileApiApi);
    }

    @Test
    void getFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String pathToFile = "folder/file.txt";
        final int expireInMinutes = 10;
        final String presignedUrl = "the_presignedUrl";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, expireInMinutes)).thenReturn(presignedUrl);
        Mockito.when(this.s3FileTransferRepository.getFile(presignedUrl)).thenReturn(new byte[]{});
        this.documentStorageFileRepository.getFile(pathToFile, expireInMinutes);

        Mockito.verify(this.presignedUrlRepository, Mockito.times(1)).getPresignedUrlGetFile(pathToFile, expireInMinutes);
        Mockito.verify(this.s3FileTransferRepository, Mockito.times(1)).getFile(presignedUrl);
    }

    @Test
    void saveFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String pathToFile = "folder/file.txt";
        final byte[] file = new byte[]{ 1, 2, 3, 4, 5, 6, 7 };
        final int expireInMinutes = 10;
        final LocalDate endOfLifeFolder = LocalDate.now();
        final String presignedUrl = "the_presignedUrl";


        Mockito.when(this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, expireInMinutes, endOfLifeFolder)).thenReturn(presignedUrl);
        Mockito.doNothing().when(this.s3FileTransferRepository).saveFile(presignedUrl, file);
        this.documentStorageFileRepository.saveFile(pathToFile, file, expireInMinutes, endOfLifeFolder);

        Mockito.verify(this.presignedUrlRepository, Mockito.times(1)).getPresignedUrlSaveFile(pathToFile, expireInMinutes, endOfLifeFolder);
        Mockito.verify(this.s3FileTransferRepository, Mockito.times(1)).saveFile(presignedUrl, file);
    }

    @Test
    void updateFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String pathToFile = "folder/file.txt";
        final byte[] file = new byte[]{ 1, 2, 3, 4, 5, 6, 7 };
        final int expireInMinutes = 10;
        final LocalDate endOfLifeFolder = LocalDate.now();
        final String presignedUrl = "the_presignedUrl";


        Mockito.when(this.presignedUrlRepository.getPresignedUrlUpdateFile(pathToFile, expireInMinutes, endOfLifeFolder)).thenReturn(presignedUrl);
        Mockito.doNothing().when(this.s3FileTransferRepository).updateFile(presignedUrl, file);
        this.documentStorageFileRepository.updateFile(pathToFile, file, expireInMinutes, endOfLifeFolder);

        Mockito.verify(this.presignedUrlRepository, Mockito.times(1)).getPresignedUrlUpdateFile(pathToFile, expireInMinutes, endOfLifeFolder);
        Mockito.verify(this.s3FileTransferRepository, Mockito.times(1)).updateFile(presignedUrl, file);
    }

    @Test
    void deleteFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String pathToFile = "folder/file.txt";
        final Integer expireInMinutes = 10;
        final String presignedUrl = "the_presignedUrl";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, expireInMinutes)).thenReturn(presignedUrl);
        Mockito.doNothing().when(this.s3FileTransferRepository).deleteFile(presignedUrl);
        this.documentStorageFileRepository.deleteFile(pathToFile, expireInMinutes);

        Mockito.verify(this.presignedUrlRepository, Mockito.times(1)).getPresignedUrlDeleteFile(pathToFile, expireInMinutes);
        Mockito.verify(this.s3FileTransferRepository, Mockito.times(1)).deleteFile(presignedUrl);
    }

}