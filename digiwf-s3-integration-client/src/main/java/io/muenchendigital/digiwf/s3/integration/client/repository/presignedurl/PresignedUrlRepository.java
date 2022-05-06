package io.muenchendigital.digiwf.s3.integration.client.repository.presignedurl;

import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.model.DefaultDocumentStorageUrl;
import io.muenchendigital.digiwf.s3.integration.gen.api.FileApiApi;
import io.muenchendigital.digiwf.s3.integration.gen.model.FileDataDto;
import io.muenchendigital.digiwf.s3.integration.gen.model.PresignedUrlDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PresignedUrlRepository {

    private final FileApiApi fileApi;

    private final DefaultDocumentStorageUrl defaultDocumentStorageUrl;

    /**
     * Fetches a presignedURL for the file named in the parameter to get a file from the document storage.
     *
     * @param pathToFile defines the path to the file.
     * @param expireInMinutes the expiration time of the presignedURL in minutes.
     * @return the presignedURL.
     * @throws DocumentStorageClientErrorException if the problem is with the client.
     * @throws DocumentStorageServerErrorException if the problem is with the document storage.
     * @throws DocumentStorageException            if the problem cannot be assigned to either the client or the document storage.
     */
    public String getPresignedUrlGetFile(final String pathToFile, final int expireInMinutes) throws DocumentStorageClientErrorException, DocumentStorageServerErrorException, DocumentStorageException {
        return this.getPresignedUrlGetFile(
                pathToFile,
                expireInMinutes,
                this.defaultDocumentStorageUrl.getDocumentStorageUrl()
        );
    }

    /**
     * Fetches a presignedURL for the file named in the parameter to get a file from the document storage.
     *
     * @param pathToFile defines the path to the file.
     * @param expireInMinutes the expiration time of the presignedURL in minutes.
     * @param documentStorageUrl to define which document should be requested.
     * @return the presignedURL.
     * @throws DocumentStorageClientErrorException if the problem is with the client.
     * @throws DocumentStorageServerErrorException if the problem is with the document storage.
     * @throws DocumentStorageException            if the problem cannot be assigned to either the client or the document storage.
     */
    public String getPresignedUrlGetFile(final String pathToFile, final int expireInMinutes, final String documentStorageUrl) throws DocumentStorageClientErrorException, DocumentStorageServerErrorException, DocumentStorageException {
        try {
            this.fileApi.getApiClient().setBasePath(documentStorageUrl);
            final PresignedUrlDto presignedUrlDto = this.fileApi.get(pathToFile, expireInMinutes);
            return presignedUrlDto.getUrl();
        } catch (final HttpClientErrorException exception) {
            final String message = String.format("The request to create a presigned url to get a file failed %s.", exception.getStatusCode());
            log.error(message);
            throw new DocumentStorageClientErrorException(message, exception);
        } catch (final HttpServerErrorException exception) {
            final String message = String.format("The request to create a presigned url to get a file failed %s.", exception.getStatusCode());
            log.error(message);
            throw new DocumentStorageServerErrorException(message, exception);
        } catch (final RestClientException exception) {
            final String message = String.format("The request to create a presigned url to get a file failed.");
            log.error(message);
            throw new DocumentStorageException(message, exception);
        }
    }

    /**
     * Fetches a presignedURL for the file named in the parameter to store a file in the document storage.
     *
     * @param pathToFile defines the path to the file.
     * @param expireInMinutes the expiration time of the presignedURL in minutes.
     * @param endOfLifeFolder the end of life of the folder defined in refId. May be null.
     * @return the presignedURL.
     * @throws DocumentStorageClientErrorException if the problem is with the client.
     * @throws DocumentStorageServerErrorException if the problem is with the document storage.
     * @throws DocumentStorageException            if the problem cannot be assigned to either the client or the document storage.
     */
    public String getPresignedUrlSaveFile(final String pathToFile, final int expireInMinutes, final LocalDate endOfLifeFolder) throws DocumentStorageClientErrorException, DocumentStorageServerErrorException, DocumentStorageException {
        return this.getPresignedUrlSaveFile(
                pathToFile,
                expireInMinutes,
                endOfLifeFolder,
                this.defaultDocumentStorageUrl.getDocumentStorageUrl()
        );
    }

    /**
     * Fetches a presignedURL for the file named in the parameter to store a file in the document storage.
     *
     * @param pathToFile defines the path to the file.
     * @param expireInMinutes the expiration time of the presignedURL in minutes.
     * @param endOfLifeFolder the end of life of the folder defined in refId. May be null.
     * @param documentStorageUrl to define which document should be requested.
     * @return the presignedURL.
     * @throws DocumentStorageClientErrorException if the problem is with the client.
     * @throws DocumentStorageServerErrorException if the problem is with the document storage.
     * @throws DocumentStorageException            if the problem cannot be assigned to either the client or the document storage.
     */
    public String getPresignedUrlSaveFile(final String pathToFile, final int expireInMinutes, final LocalDate endOfLifeFolder, final String documentStorageUrl) throws DocumentStorageClientErrorException, DocumentStorageServerErrorException, DocumentStorageException {
        try {
            this.fileApi.getApiClient().setBasePath(documentStorageUrl);
            final var fileDataDto = new FileDataDto();
            fileDataDto.setPathToFile(pathToFile);
            fileDataDto.setExpiresInMinutes(expireInMinutes);
            fileDataDto.setEndOfLife(endOfLifeFolder);
            final PresignedUrlDto presignedUrlDto = this.fileApi.save(fileDataDto);
            return presignedUrlDto.getUrl();
        } catch (final HttpClientErrorException exception) {
            final String message = String.format("The request to create a presigned save url failed %s.", exception.getStatusCode());
            log.error(message);
            throw new DocumentStorageClientErrorException(message, exception);
        } catch (final HttpServerErrorException exception) {
            final String message = String.format("The request to create a presigned save url failed %s.", exception.getStatusCode());
            log.error(message);
            throw new DocumentStorageServerErrorException(message, exception);
        } catch (final RestClientException exception) {
            final String message = String.format("The request to create a presigned save url failed.");
            log.error(message);
            throw new DocumentStorageException(message, exception);
        }
    }

    /**
     * Fetches a presignedURL for the file named in the parameter to update a file in the document storage.
     *
     * @param pathToFile defines the path to the file.
     * @param expireInMinutes the expiration time of the presignedURL in minutes.
     * @param endOfLifeFolder the end of life of the folder defined in refId. May be null.
     * @return the presignedURL.
     * @throws DocumentStorageClientErrorException if the problem is with the client.
     * @throws DocumentStorageServerErrorException if the problem is with the document storage.
     * @throws DocumentStorageException            if the problem cannot be assigned to either the client or the document storage.
     */
    public String getPresignedUrlUpdateFile(final String pathToFile, final int expireInMinutes, final LocalDate endOfLifeFolder) throws DocumentStorageClientErrorException, DocumentStorageServerErrorException, DocumentStorageException {
        return this.getPresignedUrlUpdateFile(
                pathToFile,
                expireInMinutes,
                endOfLifeFolder,
                this.defaultDocumentStorageUrl.getDocumentStorageUrl()
        );
    }

    /**
     * Fetches a presignedURL for the file named in the parameter to update a file in the document storage.
     *
     * @param pathToFile defines the path to the file.
     * @param expireInMinutes the expiration time of the presignedURL in minutes.
     * @param endOfLifeFolder the end of life of the folder defined in refId. May be null.
     * @param documentStorageUrl to define which document should be requested.
     * @return the presignedURL.
     * @throws DocumentStorageClientErrorException if the problem is with the client.
     * @throws DocumentStorageServerErrorException if the problem is with the document storage.
     * @throws DocumentStorageException            if the problem cannot be assigned to either the client or the document storage.
     */
    public String getPresignedUrlUpdateFile(final String pathToFile, final int expireInMinutes, final LocalDate endOfLifeFolder, final String documentStorageUrl) throws DocumentStorageClientErrorException, DocumentStorageServerErrorException, DocumentStorageException {
        try {
            this.fileApi.getApiClient().setBasePath(documentStorageUrl);
            final var fileDataDto = new FileDataDto();
            fileDataDto.setPathToFile(pathToFile);
            fileDataDto.setExpiresInMinutes(expireInMinutes);
            fileDataDto.setEndOfLife(endOfLifeFolder);
            final PresignedUrlDto presignedUrlDto = this.fileApi.update(fileDataDto);
            return presignedUrlDto.getUrl();
        } catch (final HttpClientErrorException exception) {
            final String message = String.format("The request to create a presigned update url failed %s.", exception.getStatusCode());
            log.error(message);
            throw new DocumentStorageClientErrorException(message, exception);
        } catch (final HttpServerErrorException exception) {
            final String message = String.format("The request to create a presigned update url failed %s.", exception.getStatusCode());
            log.error(message);
            throw new DocumentStorageServerErrorException(message, exception);
        } catch (final RestClientException exception) {
            final String message = String.format("The request to create a presigned update url failed.");
            log.error(message);
            throw new DocumentStorageException(message, exception);
        }
    }

    /**
     * Fetches a presignedURL for the file named in the parameter to delete a file from the document storage.
     *
     * @param pathToFile defines the path to the file.
     * @param expireInMinutes the expiration time of the presignedURL in minutes.
     * @return the presignedURL.
     * @throws DocumentStorageClientErrorException if the problem is with the client.
     * @throws DocumentStorageServerErrorException if the problem is with the document storage.
     * @throws DocumentStorageException            if the problem cannot be assigned to either the client or the document storage.
     */
    public String getPresignedUrlDeleteFile(final String pathToFile, final int expireInMinutes) throws DocumentStorageClientErrorException, DocumentStorageServerErrorException, DocumentStorageException {
        return this.getPresignedUrlDeleteFile(
                pathToFile,
                expireInMinutes,
                this.defaultDocumentStorageUrl.getDocumentStorageUrl()
        );
    }

    /**
     * Fetches a presignedURL for the file named in the parameter to delete a file from the document storage.
     *
     * @param pathToFile defines the path to the file.
     * @param expireInMinutes the expiration time of the presignedURL in minutes.
     * @param documentStorageUrl to define which document should be requested.
     * @return the presignedURL.
     * @throws DocumentStorageClientErrorException if the problem is with the client.
     * @throws DocumentStorageServerErrorException if the problem is with the document storage.
     * @throws DocumentStorageException            if the problem cannot be assigned to either the client or the document storage.
     */
    public String getPresignedUrlDeleteFile(final String pathToFile, final int expireInMinutes, final String documentStorageUrl) throws DocumentStorageClientErrorException, DocumentStorageServerErrorException, DocumentStorageException {
        try {
            this.fileApi.getApiClient().setBasePath(documentStorageUrl);
            final PresignedUrlDto presignedUrlDto = this.fileApi.delete1(pathToFile, expireInMinutes);
            return presignedUrlDto.getUrl();
        } catch (final HttpClientErrorException exception) {
            final String message = String.format("The request to create a presigned url to delete a file failed %s.", exception.getStatusCode());
            log.error(message);
            throw new DocumentStorageClientErrorException(message, exception);
        } catch (final HttpServerErrorException exception) {
            final String message = String.format("The request to create a presigned url to delete a file failed %s.", exception.getStatusCode());
            log.error(message);
            throw new DocumentStorageServerErrorException(message, exception);
        } catch (final RestClientException exception) {
            final String message = String.format("The request to create a presigned url to delete a file failed.");
            log.error(message);
            throw new DocumentStorageException(message, exception);
        }
    }

}
