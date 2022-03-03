package io.muenchendigital.digiwf.s3.integration.client.repository;

import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.gen.api.FolderApiApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class DocumentStorageFolderRepositoryTest {

    @Mock
    private FolderApiApi folderApi;

    private DocumentStorageFolderRepository documentStorageFolderRepository;

    @BeforeEach
    public void beforeEach() {
        this.documentStorageFolderRepository = new DocumentStorageFolderRepository(this.folderApi);
        Mockito.reset(this.folderApi);
    }

    @Test
    void deleteFolder() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";

        Mockito.doNothing().when(this.folderApi).delete(refId);
        this.documentStorageFolderRepository.deleteFolder(refId);
        Mockito.verify(this.folderApi, Mockito.times(1)).delete(refId);

        Mockito.reset(this.folderApi);
        Mockito.doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(this.folderApi).delete(refId);
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.documentStorageFolderRepository.deleteFolder(refId));
        Mockito.verify(this.folderApi, Mockito.times(1)).delete(refId);

        Mockito.reset(this.folderApi);
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(this.folderApi).delete(refId);
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.documentStorageFolderRepository.deleteFolder(refId));
        Mockito.verify(this.folderApi, Mockito.times(1)).delete(refId);

        Mockito.reset(this.folderApi);
        Mockito.doThrow(new RestClientException("Something happened")).when(this.folderApi).delete(refId);
        Assertions.assertThrows(DocumentStorageException.class, () -> this.documentStorageFolderRepository.deleteFolder(refId));
        Mockito.verify(this.folderApi, Mockito.times(1)).delete(refId);
    }

    @Test
    void updateEndOfLife() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";
        final LocalDate endOfLife = LocalDate.now();

        Mockito.doNothing().when(this.folderApi).updateEndOfLife(refId, endOfLife);
        this.documentStorageFolderRepository.updateEndOfLife(refId, endOfLife);
        Mockito.verify(this.folderApi, Mockito.times(1)).updateEndOfLife(refId, endOfLife);


        Mockito.reset(this.folderApi);
        Mockito.doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(this.folderApi).updateEndOfLife(refId, endOfLife);
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.documentStorageFolderRepository.updateEndOfLife(refId, endOfLife));
        Mockito.verify(this.folderApi, Mockito.times(1)).updateEndOfLife(refId, endOfLife);

        Mockito.reset(this.folderApi);
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(this.folderApi).updateEndOfLife(refId, endOfLife);
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.documentStorageFolderRepository.updateEndOfLife(refId, endOfLife));
        Mockito.verify(this.folderApi, Mockito.times(1)).updateEndOfLife(refId, endOfLife);

        Mockito.reset(this.folderApi);
        Mockito.doThrow(new RestClientException("Something happened")).when(this.folderApi).updateEndOfLife(refId, endOfLife);
        Assertions.assertThrows(DocumentStorageException.class, () -> this.documentStorageFolderRepository.updateEndOfLife(refId, endOfLife));
        Mockito.verify(this.folderApi, Mockito.times(1)).updateEndOfLife(refId, endOfLife);
    }

}
