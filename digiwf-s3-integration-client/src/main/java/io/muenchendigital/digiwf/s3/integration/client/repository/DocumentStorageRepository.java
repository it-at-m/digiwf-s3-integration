package io.muenchendigital.digiwf.s3.integration.client.repository;

import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageClientErrorException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageException;
import io.muenchendigital.digiwf.s3.integration.client.exception.DocumentStorageServerErrorException;
import io.muenchendigital.digiwf.s3.integration.client.repository.presignedurl.PresignedUrlRepository;
import io.muenchendigital.digiwf.s3.integration.client.repository.transfer.S3FileTransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DocumentStorageRepository {

    private final PresignedUrlRepository presignedUrlRepository;

    private final S3FileTransferRepository s3FileTransferRepository;

    /**
     * Saves the file specified in the parameter to the document storage.
     *
     * @param refId    which defines the folder in the document storage where the file will be stored.
     * @param filename in the document storage.
     * @param file         to save.
     * @param expireInMinutes the expiration time of the presignedURL in minutes.
     * @param endOfLifeFolder the end of life of the folder defined in refId.
     * @throws DocumentStorageClientErrorException if the problem is with the client.
     * @throws DocumentStorageServerErrorException if the problem is with the S3 storage or document storage.
     * @throws DocumentStorageException            if the problem cannot be assigned to either the client or the S3 storage or the document storage.
     */
    public void saveFile(final String refId, final String filename, final byte[] file, final int expireInMinutes, final LocalDate endOfLifeFolder) throws DocumentStorageException, DocumentStorageClientErrorException, DocumentStorageServerErrorException {
        final String presignedUrl = this.presignedUrlRepository.getPresignedUrlSaveFile(refId, filename, expireInMinutes, endOfLifeFolder);
        this.s3FileTransferRepository.saveFile(presignedUrl, file);
    }

}
