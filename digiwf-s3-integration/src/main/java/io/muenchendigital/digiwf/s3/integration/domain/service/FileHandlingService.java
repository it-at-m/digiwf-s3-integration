package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.domain.exception.FileExistanceException;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileData;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileResource;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileResponse;
import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FileHandlingService {

    private final S3Repository s3Repository;

    private final FolderRepository folderRepository;

    /**
     * Holt die in den Parameter angegebene Datei aus dem S3-Storage.
     *
     * @param refId    identifiziert den Namen des Ordners in welchem die Datei gespeichert ist.
     * @param fileName identifiziert den Dateinamen.
     * @return die Datei als {@link FileResource}.
     * @throws FileExistanceException falls die Datei nicht im Ordner existiert.
     * @throws S3AccessException      falls nicht auf den S3-Storage zugegriffen werden kann.
     */
    public FileResource getFile(final String refId, final String fileName) throws FileExistanceException, S3AccessException {
        final String pathToFolder = refId;
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileName
        );
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (filepathesInFolder.contains(pathToFile)) {
            final InputStream file = this.s3Repository.downloadFile(pathToFile);
            final Resource resource = new InputStreamResource(file);
            final var fileResource = new FileResource();
            fileResource.setResource(resource);
            fileResource.setFilename(fileName);
            return fileResource;
        } else {
            throw new FileExistanceException("Die Datei existiert nicht.");
        }
    }

    /**
     * Erstellt eine Presigned-URL zum Holen der im Parameter angegebenen Datei aus dem S3-Storage.
     *
     * @param refId            identifiziert den Namen des Ordners in welchem die Datei gespeichert ist.
     * @param fileName         identifiziert den Dateinamen.
     * @param expiresInMinutes zur Definition des Gültigkeitszeitraums der Presigned-URL.
     * @throws FileExistanceException falls die Datei nicht im Ordner existiert.
     * @throws S3AccessException      falls nicht auf den S3-Storage zugegriffen werden kann.
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
            throw new FileExistanceException("Die Datei existiert nicht.");
        }
    }

    /**
     * Erstellt eine Presigned-URL zum Speichern der im Parameter angegebenen Datei im S3-Storage.
     * Die Datei darf noch nicht existieren.
     *
     * @param fileData         mit den Dateimetadaten zum erneuten Speichern.
     * @param expiresInMinutes zur Definition des Gültigkeitszeitraums der Presigned-URL.
     * @throws FileExistanceException falls die Datei bereits existiert.
     * @throws S3AccessException      falls nicht auf den S3-Storage zugegriffen werden kann.
     */
    public FileResponse saveFile(final FileData fileData, final int expiresInMinutes) throws FileExistanceException, S3AccessException {
        final String pathToFolder = fileData.getRefId();
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileData.getFilename()
        );
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (filepathesInFolder.contains(pathToFile)) {
            throw new FileExistanceException("Die Datei existiert bereits.");
        } else {
            return this.updateFile(fileData, expiresInMinutes);
        }
    }

    /**
     * Erstellt eine Presigned-URL zum Überschreiben der im Parameter angegebenen Datei im S3-Storage.
     * Des Weiteren wird in der Datenbank der Eintrag bezüglich {@link Folder#getEndOfLife()} angepasst.
     * <p>
     * Gibt es die Datei noch nicht im S3-Storage, so wird diese neu angelegt und in der Datenbank
     * ein entsprechender {@link Folder} persistiert.
     *
     * @param fileData         mit den Dateimetadaten zum erneuten Speichern.
     * @param expiresInMinutes zur Definition des Gültigkeitszeitraums der Presigned-URL.
     * @throws S3AccessException falls nicht auf den S3-Storage zugegriffen werden kann.
     */
    public FileResponse updateFile(final FileData fileData, final int expiresInMinutes) throws S3AccessException {
        final String pathToFolder = fileData.getRefId();
        final Optional<Folder> folderOptional = this.folderRepository.findByRefId(pathToFolder);
        if (folderOptional.isEmpty()) {
            // Fall noch kein Ordner existiert.
            final var folder = new Folder();
            folder.setRefId(fileData.getRefId());
            folder.setEndOfLife(fileData.getEndOfLife());
            this.folderRepository.save(folder);
        } else if (FileHandlingService.shouldNewEndOfLifeBeSet(fileData, folderOptional.get())) {
            // Beim Hochladen in bereits existierenden Ordner
            final Folder folder = folderOptional.get();
            folder.setEndOfLife(fileData.getEndOfLife());
            this.folderRepository.save(folder);
        }
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileData.getFilename()
        );
        final String presignedUrl = this.s3Repository.getPresignedUrlForFileUpload(pathToFile, expiresInMinutes);
        return new FileResponse(presignedUrl);
    }

    /**
     * Löscht die in den Parameter angegebene Datei aus dem S3-Storage.
     *
     * @param refId    identifiziert den Namen des Ordners in welchem die Datei gespeichert ist.
     * @param fileName identifiziert den Dateinamen.
     * @throws FileExistanceException falls die Datei nicht im Ordner existiert.
     * @throws S3AccessException      falls nicht auf den S3-Storage zugegriffen werden kann.
     */
    public void deleteFile(final String refId, final String fileName) throws FileExistanceException, S3AccessException {
        final String pathToFolder = refId;
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileName
        );
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (filepathesInFolder.contains(pathToFile)) {
            this.s3Repository.deleteFile(pathToFile);
        } else {
            throw new FileExistanceException("Die Datei existiert nicht.");
        }
    }

    /**
     * Erstellt eine Presigned-URL zum Löschen der im Parameter angegebenen Datei aus dem S3-Storage.
     *
     * @param refId            identifiziert den Namen des Ordners in welchem die Datei gespeichert ist.
     * @param fileName         identifiziert den Dateinamen.
     * @param expiresInMinutes zur Definition des Gültigkeitszeitraums der Presigned-URL.
     * @throws FileExistanceException falls die Datei nicht im Ordner existiert.
     * @throws S3AccessException      falls nicht auf den S3-Storage zugegriffen werden kann.
     */
    public FileResponse deleteFile(final String refId, final String fileName, final int expiresInMinutes) throws FileExistanceException, S3AccessException {
        final String pathToFolder = refId;
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileName
        );
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (filepathesInFolder.contains(pathToFile)) {
            final String presignedUrl = this.s3Repository.getPresignedUrlForFileDeletion(pathToFile, expiresInMinutes);
            return new FileResponse(presignedUrl);
        } else {
            throw new FileExistanceException("Die Datei existiert nicht.");
        }
    }

    public static String createFilePath(final String pathToFolder, final String fileName) {
        return pathToFolder + "/" + fileName;
    }

    /**
     * Die Methode prüft, ob ein überschrieben des Parameter {@link Folder#getEndOfLife()} erforderlich ist.
     *
     * @param fileData mit neuem {@link FileData#getEndOfLife()}
     * @param folder   mit aktuellen {@link Folder#getEndOfLife()}
     * @return true falls bei beiden Datum gesetzt, bzw. true falls {@link FileData#getEndOfLife()} gesetzt
     * und {@link Folder#getEndOfLife()} nicht gesetzt. Andernfalls false.
     */
    public static boolean shouldNewEndOfLifeBeSet(final FileData fileData, final Folder folder) {
        return  // End Of Life fileData gesetzt und folder gesetzt -> Erfordert Datumsprüfung
                (ObjectUtils.isNotEmpty(fileData.getEndOfLife())
                        && ObjectUtils.isNotEmpty(folder.getEndOfLife())
                        && fileData.getEndOfLife().isAfter(folder.getEndOfLife()))
                        // End Of Life fileData gesetzt und folder nicht gesetzt
                        || (ObjectUtils.isNotEmpty(fileData.getEndOfLife())
                        && ObjectUtils.isEmpty(folder.getEndOfLife()));
    }

}
