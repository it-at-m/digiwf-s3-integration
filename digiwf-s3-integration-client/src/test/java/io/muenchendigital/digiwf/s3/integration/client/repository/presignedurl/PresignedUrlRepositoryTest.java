package io.muenchendigital.digiwf.s3.integration.client.repository.presignedurl;

import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.model.DefaultDocumentStorageUrl;
import io.muenchendigital.digiwf.s3.integration.gen.ApiClient;
import io.muenchendigital.digiwf.s3.integration.gen.api.FileApiApi;
import io.muenchendigital.digiwf.s3.integration.gen.model.FileDataDto;
import io.muenchendigital.digiwf.s3.integration.gen.model.PresignedUrlDto;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class PresignedUrlRepositoryTest {

    private final DefaultDocumentStorageUrl defaultDocumentStorageUrl = new DefaultDocumentStorageUrl("the-url");

    @Mock
    private FileApiApi fileApi;

    private PresignedUrlRepository presignedUrlRepository;

    @BeforeEach
    public void beforeEach() {
        this.presignedUrlRepository = new PresignedUrlRepository(this.fileApi, this.defaultDocumentStorageUrl);
        Mockito.reset(this.fileApi);
    }

    @Test
    void getPresignedUrlGetFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String pathToFile = "folder/file.txt";
        final Integer expireInMinutes = 10;

        final PresignedUrlDto expected = new PresignedUrlDto();
        expected.setUrl("the_presignedUrl");

        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.get(pathToFile, expireInMinutes)).thenReturn(expected);
        final String result = this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, expireInMinutes);
        Mockito.verify(this.fileApi, Mockito.times(1)).get(pathToFile, expireInMinutes);
        assertThat(result, is(expected.getUrl()));
        Mockito.reset(this.fileApi);
    }

    @Test
    void getPresignedUrlGetFileException() {
        final String pathToFile = "folder/file.txt";
        final Integer expireInMinutes = 10;

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.get(pathToFile, expireInMinutes)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).get(pathToFile, expireInMinutes);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.get(pathToFile, expireInMinutes)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).get(pathToFile, expireInMinutes);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.get(pathToFile, expireInMinutes)).thenThrow(new RestClientException("Something happened"));
        Assertions.assertThrows(DocumentStorageException.class, () -> this.presignedUrlRepository.getPresignedUrlGetFile(pathToFile, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).get(pathToFile, expireInMinutes);
    }

    @Test
    void getPresignedUrlSaveFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String pathToFile = "folder/file.txt";
        final Integer expireInMinutes = 10;
        final LocalDate endOfLife = LocalDate.now();

        final FileDataDto fileDataDto = new FileDataDto();
        fileDataDto.setPathToFile(pathToFile);
        fileDataDto.setExpiresInMinutes(expireInMinutes);
        fileDataDto.setEndOfLife(endOfLife);

        final PresignedUrlDto expected = new PresignedUrlDto();
        expected.setUrl("the_presignedUrl");

        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.save(fileDataDto)).thenReturn(expected);

        final String result = this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, expireInMinutes, endOfLife);
        Mockito.verify(this.fileApi, Mockito.times(1)).save(fileDataDto);
        assertThat(result, is(expected.getUrl()));
    }

    @Test
    void getPresignedUrlSaveFileException() {
        final String pathToFile = "folder/file.txt";
        final Integer expireInMinutes = 10;
        final LocalDate endOfLife = LocalDate.now();

        final FileDataDto fileDataDto = new FileDataDto();
        fileDataDto.setPathToFile(pathToFile);
        fileDataDto.setExpiresInMinutes(expireInMinutes);
        fileDataDto.setEndOfLife(endOfLife);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.save(fileDataDto)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).save(fileDataDto);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.save(fileDataDto)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).save(fileDataDto);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.save(fileDataDto)).thenThrow(new RestClientException("Something happened"));
        Assertions.assertThrows(DocumentStorageException.class, () -> this.presignedUrlRepository.getPresignedUrlSaveFile(pathToFile, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).save(fileDataDto);
    }

    @Test
    void getPresignedUrlUpdateFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String pathToFile = "folder/file.txt";
        final Integer expireInMinutes = 10;
        final LocalDate endOfLife = LocalDate.now();

        final FileDataDto fileDataDto = new FileDataDto();
        fileDataDto.setPathToFile(pathToFile);
        fileDataDto.setExpiresInMinutes(expireInMinutes);
        fileDataDto.setEndOfLife(endOfLife);

        final PresignedUrlDto expected = new PresignedUrlDto();
        expected.setUrl("the_presignedUrl");

        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.update(fileDataDto)).thenReturn(expected);

        final String result = this.presignedUrlRepository.getPresignedUrlUpdateFile(pathToFile, expireInMinutes, endOfLife);
        Mockito.verify(this.fileApi, Mockito.times(1)).update(fileDataDto);
        assertThat(result, is(expected.getUrl()));
    }

    @Test
    void getPresignedUrlUpdateFileException() {
        final String pathToFile = "folder/file.txt";
        final Integer expireInMinutes = 10;
        final LocalDate endOfLife = LocalDate.now();

        final FileDataDto fileDataDto = new FileDataDto();
        fileDataDto.setPathToFile(pathToFile);
        fileDataDto.setExpiresInMinutes(expireInMinutes);
        fileDataDto.setEndOfLife(endOfLife);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.update(fileDataDto)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlUpdateFile(pathToFile, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).update(fileDataDto);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.update(fileDataDto)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlUpdateFile(pathToFile, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).update(fileDataDto);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.update(fileDataDto)).thenThrow(new RestClientException("Something happened"));
        Assertions.assertThrows(DocumentStorageException.class, () -> this.presignedUrlRepository.getPresignedUrlUpdateFile(pathToFile, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).update(fileDataDto);
    }

    @Test
    void getPresignedUrlDeleteFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String pathToFile = "folder/file.txt";
        final Integer expireInMinutes = 10;

        final PresignedUrlDto expected = new PresignedUrlDto();
        expected.setUrl("the_presignedUrl");

        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.delete1(pathToFile, expireInMinutes)).thenReturn(expected);

        final String result = this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, expireInMinutes);
        Mockito.verify(this.fileApi, Mockito.times(1)).delete1(pathToFile, expireInMinutes);
        assertThat(result, is(expected.getUrl()));
    }

    @Test
    void getPresignedUrlDeleteFileException() {
        final String pathToFile = "folder/file.txt";
        final Integer expireInMinutes = 10;

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.delete1(pathToFile, expireInMinutes)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).delete1(pathToFile, expireInMinutes);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.delete1(pathToFile, expireInMinutes)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).delete1(pathToFile, expireInMinutes);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.getApiClient()).thenReturn(new ApiClient());
        Mockito.when(this.fileApi.delete1(pathToFile, expireInMinutes)).thenThrow(new RestClientException("Something happened"));
        Assertions.assertThrows(DocumentStorageException.class, () -> this.presignedUrlRepository.getPresignedUrlDeleteFile(pathToFile, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).delete1(pathToFile, expireInMinutes);
    }

}
