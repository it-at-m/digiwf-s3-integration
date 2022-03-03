package io.muenchendigital.digiwf.s3.integration.client.repository.presignedurl;

import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
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

    @Mock
    private FileApiApi fileApi;

    private PresignedUrlRepository presignedUrlRepository;

    @BeforeEach
    public void beforeEach() {
        this.presignedUrlRepository = new PresignedUrlRepository(this.fileApi);
        Mockito.reset(this.fileApi);
    }

    @Test
    void getPresignedUrlGetFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final Integer expireInMinutes = 10;

        final PresignedUrlDto expected = new PresignedUrlDto();
        expected.setUrl("the_presignedUrl");

        Mockito.when(this.fileApi.get(refId, fileName, expireInMinutes)).thenReturn(expected);
        final String result = this.presignedUrlRepository.getPresignedUrlGetFile(refId, fileName, expireInMinutes);
        Mockito.verify(this.fileApi, Mockito.times(1)).get(refId, fileName, expireInMinutes);
        assertThat(result, is(expected.getUrl()));
        Mockito.reset(this.fileApi);
    }

    @Test
    void getPresignedUrlGetFileException() {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final Integer expireInMinutes = 10;

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.get(refId, fileName, expireInMinutes)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlGetFile(refId, fileName, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).get(refId, fileName, expireInMinutes);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.get(refId, fileName, expireInMinutes)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlGetFile(refId, fileName, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).get(refId, fileName, expireInMinutes);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.get(refId, fileName, expireInMinutes)).thenThrow(new RestClientException("Something happened"));
        Assertions.assertThrows(DocumentStorageException.class, () -> this.presignedUrlRepository.getPresignedUrlGetFile(refId, fileName, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).get(refId, fileName, expireInMinutes);
    }

    @Test
    void getPresignedUrlSaveFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final Integer expireInMinutes = 10;
        final LocalDate endOfLife = LocalDate.now();

        final FileDataDto fileDataDto = new FileDataDto();
        fileDataDto.setFilename(fileName);
        fileDataDto.setRefId(refId);
        fileDataDto.setExpiresInMinutes(expireInMinutes);
        fileDataDto.setEndOfLife(endOfLife);

        final PresignedUrlDto expected = new PresignedUrlDto();
        expected.setUrl("the_presignedUrl");

        Mockito.when(this.fileApi.save(fileDataDto)).thenReturn(expected);

        final String result = this.presignedUrlRepository.getPresignedUrlSaveFile(refId, fileName, expireInMinutes, endOfLife);
        Mockito.verify(this.fileApi, Mockito.times(1)).save(fileDataDto);
        assertThat(result, is(expected.getUrl()));
    }

    @Test
    void getPresignedUrlSaveFileException() {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final Integer expireInMinutes = 10;
        final LocalDate endOfLife = LocalDate.now();

        final FileDataDto fileDataDto = new FileDataDto();
        fileDataDto.setFilename(fileName);
        fileDataDto.setRefId(refId);
        fileDataDto.setExpiresInMinutes(expireInMinutes);
        fileDataDto.setEndOfLife(endOfLife);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.save(fileDataDto)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlSaveFile(refId, fileName, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).save(fileDataDto);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.save(fileDataDto)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlSaveFile(refId, fileName, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).save(fileDataDto);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.save(fileDataDto)).thenThrow(new RestClientException("Something happened"));
        Assertions.assertThrows(DocumentStorageException.class, () -> this.presignedUrlRepository.getPresignedUrlSaveFile(refId, fileName, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).save(fileDataDto);
    }

    @Test
    void getPresignedUrlUpdateFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final Integer expireInMinutes = 10;
        final LocalDate endOfLife = LocalDate.now();

        final FileDataDto fileDataDto = new FileDataDto();
        fileDataDto.setFilename(fileName);
        fileDataDto.setRefId(refId);
        fileDataDto.setExpiresInMinutes(expireInMinutes);
        fileDataDto.setEndOfLife(endOfLife);

        final PresignedUrlDto expected = new PresignedUrlDto();
        expected.setUrl("the_presignedUrl");

        Mockito.when(this.fileApi.update(fileDataDto)).thenReturn(expected);

        final String result = this.presignedUrlRepository.getPresignedUrlUpdateFile(refId, fileName, expireInMinutes, endOfLife);
        Mockito.verify(this.fileApi, Mockito.times(1)).update(fileDataDto);
        assertThat(result, is(expected.getUrl()));
    }

    @Test
    void getPresignedUrlUpdateFileException() {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final Integer expireInMinutes = 10;
        final LocalDate endOfLife = LocalDate.now();

        final FileDataDto fileDataDto = new FileDataDto();
        fileDataDto.setFilename(fileName);
        fileDataDto.setRefId(refId);
        fileDataDto.setExpiresInMinutes(expireInMinutes);
        fileDataDto.setEndOfLife(endOfLife);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.update(fileDataDto)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlUpdateFile(refId, fileName, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).update(fileDataDto);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.update(fileDataDto)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlUpdateFile(refId, fileName, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).update(fileDataDto);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.update(fileDataDto)).thenThrow(new RestClientException("Something happened"));
        Assertions.assertThrows(DocumentStorageException.class, () -> this.presignedUrlRepository.getPresignedUrlUpdateFile(refId, fileName, expireInMinutes, endOfLife));
        Mockito.verify(this.fileApi, Mockito.times(1)).update(fileDataDto);
    }

    @Test
    void getPresignedUrlDeleteFile() throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final Integer expireInMinutes = 10;

        final PresignedUrlDto expected = new PresignedUrlDto();
        expected.setUrl("the_presignedUrl");

        Mockito.when(this.fileApi.delete1(refId, fileName, expireInMinutes)).thenReturn(expected);

        final String result = this.presignedUrlRepository.getPresignedUrlDeleteFile(refId, fileName, expireInMinutes);
        Mockito.verify(this.fileApi, Mockito.times(1)).delete1(refId, fileName, expireInMinutes);
        assertThat(result, is(expected.getUrl()));
    }

    @Test
    void getPresignedUrlDeleteFileException() {
        final String refId = "the_refId";
        final String fileName = "the_fileName";
        final Integer expireInMinutes = 10;

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.delete1(refId, fileName, expireInMinutes)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertThrows(DocumentStorageClientErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlDeleteFile(refId, fileName, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).delete1(refId, fileName, expireInMinutes);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.delete1(refId, fileName, expireInMinutes)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThrows(DocumentStorageServerErrorException.class, () -> this.presignedUrlRepository.getPresignedUrlDeleteFile(refId, fileName, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).delete1(refId, fileName, expireInMinutes);

        Mockito.reset(this.fileApi);
        Mockito.when(this.fileApi.delete1(refId, fileName, expireInMinutes)).thenThrow(new RestClientException("Something happened"));
        Assertions.assertThrows(DocumentStorageException.class, () -> this.presignedUrlRepository.getPresignedUrlDeleteFile(refId, fileName, expireInMinutes));
        Mockito.verify(this.fileApi, Mockito.times(1)).delete1(refId, fileName, expireInMinutes);
    }

}
