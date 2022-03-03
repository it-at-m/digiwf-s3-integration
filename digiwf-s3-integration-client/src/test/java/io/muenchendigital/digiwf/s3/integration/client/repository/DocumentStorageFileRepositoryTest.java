package io.muenchendigital.digiwf.s3.integration.client.repository;

import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.repository.presignedurl.PresignedUrlRepository;
import io.muenchendigital.digiwf.s3.integration.client.repository.transfer.S3FileTransferRepository;
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

    private DocumentStorageFileRepository documentStorageFileRepository;

    @BeforeEach
    public void beforeEach() {
        this.documentStorageFileRepository = new DocumentStorageFileRepository(this.presignedUrlRepository, this.s3FileTransferRepository);
        Mockito.reset(this.presignedUrlRepository, this.s3FileTransferRepository);
    }

    @Test
    void getFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final int expireInMinutes = 10;
        final String presignedUrl = "the_presignedUrl";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlGetFile(refId, fileName, expireInMinutes)).thenReturn(presignedUrl);
        Mockito.when(this.s3FileTransferRepository.getFile(presignedUrl)).thenReturn(new byte[]{});
        this.documentStorageFileRepository.getFile(refId, fileName, expireInMinutes);

        Mockito.verify(this.presignedUrlRepository, Mockito.times(1)).getPresignedUrlGetFile(refId, fileName, expireInMinutes);
        Mockito.verify(this.s3FileTransferRepository, Mockito.times(1)).getFile(presignedUrl);
    }

    @Test
    void saveFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final byte[] file = new byte[]{ 1, 2, 3, 4, 5, 6, 7 };
        final int expireInMinutes = 10;
        final LocalDate endOfLifeFolder = LocalDate.now();
        final String presignedUrl = "the_presignedUrl";


        Mockito.when(this.presignedUrlRepository.getPresignedUrlSaveFile(refId, fileName, expireInMinutes, endOfLifeFolder)).thenReturn(presignedUrl);
        Mockito.doNothing().when(this.s3FileTransferRepository).saveFile(presignedUrl, file);
        this.documentStorageFileRepository.saveFile(refId, fileName, file, expireInMinutes, endOfLifeFolder);

        Mockito.verify(this.presignedUrlRepository, Mockito.times(1)).getPresignedUrlSaveFile(refId, fileName, expireInMinutes, endOfLifeFolder);
        Mockito.verify(this.s3FileTransferRepository, Mockito.times(1)).saveFile(presignedUrl, file);
    }

    @Test
    void updateFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final byte[] file = new byte[]{ 1, 2, 3, 4, 5, 6, 7 };
        final int expireInMinutes = 10;
        final LocalDate endOfLifeFolder = LocalDate.now();
        final String presignedUrl = "the_presignedUrl";


        Mockito.when(this.presignedUrlRepository.getPresignedUrlUpdateFile(refId, fileName, expireInMinutes, endOfLifeFolder)).thenReturn(presignedUrl);
        Mockito.doNothing().when(this.s3FileTransferRepository).updateFile(presignedUrl, file);
        this.documentStorageFileRepository.updateFile(refId, fileName, file, expireInMinutes, endOfLifeFolder);

        Mockito.verify(this.presignedUrlRepository, Mockito.times(1)).getPresignedUrlUpdateFile(refId, fileName, expireInMinutes, endOfLifeFolder);
        Mockito.verify(this.s3FileTransferRepository, Mockito.times(1)).updateFile(presignedUrl, file);
    }

    @Test
    void deleteFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final Integer expireInMinutes = 10;
        final String presignedUrl = "the_presignedUrl";

        Mockito.when(this.presignedUrlRepository.getPresignedUrlDeleteFile(refId, fileName, expireInMinutes)).thenReturn(presignedUrl);
        Mockito.doNothing().when(this.s3FileTransferRepository).deleteFile(presignedUrl);
        this.documentStorageFileRepository.deleteFile(refId, fileName, expireInMinutes);

        Mockito.verify(this.presignedUrlRepository, Mockito.times(1)).getPresignedUrlDeleteFile(refId, fileName, expireInMinutes);
        Mockito.verify(this.s3FileTransferRepository, Mockito.times(1)).deleteFile(presignedUrl);
    }

}