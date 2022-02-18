package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.domain.exception.FileExistanceException;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileData;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileResponse;
import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileHandlingService {

    public static final int MIN_EXPIRES_IN_MINUTES = 1;

    private final S3Repository s3Repository;

    private final FolderRepository folderRepository;

    /**
     * Creates a presigned URL to fetch the file specified in the parameter from the S3 storage.
     *
     * @param refId            identifies the name of the folder in which the file is stored.
     * @param fileName         identifies the file name.
     * @param expiresInMinutes to define the validity period of the presigned URL.
     * @throws FileExistanceException if the file does not exist in the folder.
     * @throws S3AccessException      if the S3 storage cannot be accessed.
     */
    public FileResponse getFile(final String refId, final String fileName, final int expiresInMinutes) throws FileExistanceException, S3AccessException {
        final String pathToFolder = refId;
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileName
        );
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (filepathesInFolder.contains(pathToFile)) {
            final String presignedUrl = this.s3Repository.getPresignedUrlForFileDownload(pathToFile, expiresInMinutes);
            return new FileResponse(presignedUrl);
        } else {
            final String message = String.format("The file %s does not exist.", pathToFile);
            log.error(message);
            throw new FileExistanceException(message);
        }
    }

    /**
     * Creates a presigned URL to store the file specified in the parameter within the S3 storage.
     * The file must not exist yet.
     *
     * @param fileData with the file metadata to save.
     * @throws FileExistanceException if the file already exists.
     * @throws S3AccessException      if the S3 storage cannot be accessed.
     */
    public FileResponse saveFile(final FileData fileData) throws FileExistanceException, S3AccessException {
        final String pathToFolder = fileData.getRefId();
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileData.getFilename()
        );
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (filepathesInFolder.contains(pathToFile)) {
            final String message = String.format("The file %s already exists.", pathToFile);
            log.error(message);
            throw new FileExistanceException(message);
        } else {
            log.info("The new file ${} will be saved.", pathToFolder);
            return this.updateFile(fileData);
        }
    }

    /**
     * Creates a presigned URL to overwrite the file specified in the parameter within the S3 storage.
     * Furthermore, the entry regarding {@link Folder#getEndOfLife()} is adjusted in the database.
     * <p>
     * If the file does not yet exist in the S3 storage, it is newly created and a
     * corresponding {@link Folder} is persisted in the database.
     *
     * @param fileData with the file metadata for resaving.
     * @throws S3AccessException if the S3 storage cannot be accessed.
     */
    public FileResponse updateFile(final FileData fileData) throws S3AccessException {
        final String pathToFolder = fileData.getRefId();
        final Optional<Folder> folderOptional = this.folderRepository.findByRefId(pathToFolder);
        if (folderOptional.isEmpty()) {
            log.info("The database entry for folder ${} does not exist.", pathToFolder);
            final var folder = new Folder();
            folder.setRefId(fileData.getRefId());
            folder.setEndOfLife(fileData.getEndOfLife());
            this.folderRepository.save(folder);
        } else if (FileHandlingService.shouldNewEndOfLifeBeSet(fileData, folderOptional.get())) {
            log.info("The database entry for folder ${} already exists.", pathToFolder);
            final Folder folder = folderOptional.get();
            folder.setEndOfLife(fileData.getEndOfLife());
            this.folderRepository.save(folder);
        }
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileData.getFilename()
        );
        final String presignedUrl = this.s3Repository.getPresignedUrlForFileUpload(
                pathToFile,
                fileData.getExpiresInMinutes()
        );
        return new FileResponse(presignedUrl);
    }

    /**
     * Creates a presigned URL to delete the file specified in the parameter from the S3 storage.
     *
     * @param refId            identifies the name of the folder in which the file is stored.
     * @param fileName         identifies the file name.
     * @param expiresInMinutes to define the validity period of the presigned URL.
     * @throws FileExistanceException if the file does not exist in the folder.
     * @throws S3AccessException      if the S3 storage cannot be accessed.
     */
    public FileResponse deleteFile(final String refId, final String fileName, final int expiresInMinutes) throws FileExistanceException, S3AccessException {
        final String pathToFolder = refId;
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileName
        );
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (filepathesInFolder.contains(pathToFile)) {
            log.info("The file ${} exists.", pathToFile);
            final String presignedUrl = this.s3Repository.getPresignedUrlForFileDeletion(pathToFile, expiresInMinutes);
            return new FileResponse(presignedUrl);
        } else {
            final String message = String.format("The file %s does not exist.", pathToFile);
            log.error(message);
            throw new FileExistanceException(message);
        }
    }

    public static String createFilePath(final String pathToFolder, final String fileName) {
        return pathToFolder + "/" + fileName;
    }

    /**
     * The method checks if an override of the {@link Folder#getEndOfLife()} parameter is required.
     *
     * @param fileData with new {@link FileData#getEndOfLife()}
     * @param folder   with current {@link Folder#getEndOfLife()}
     * @return true if both dates are set, or true if {@link FileData#getEndOfLife()} is set
     * and {@link Folder#getEndOfLife()} is not set. Otherwise false.
     */
    public static boolean shouldNewEndOfLifeBeSet(final FileData fileData, final Folder folder) {
        return  // End Of Life in fileData set and in folder set
                (ObjectUtils.isNotEmpty(fileData.getEndOfLife())
                        && ObjectUtils.isNotEmpty(folder.getEndOfLife())
                        && fileData.getEndOfLife().isAfter(folder.getEndOfLife()))
                        // End Of Life in fileData set und in folder not set
                        || (ObjectUtils.isNotEmpty(fileData.getEndOfLife())
                        && ObjectUtils.isEmpty(folder.getEndOfLife()));
    }

}
