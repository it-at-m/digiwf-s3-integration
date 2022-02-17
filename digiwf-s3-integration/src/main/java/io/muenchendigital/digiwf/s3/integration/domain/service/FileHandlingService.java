package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.domain.exception.BrokenFileException;
import io.muenchendigital.digiwf.s3.integration.domain.exception.FileExistanceException;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileData;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileResource;
import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
     * Speichert die im Parameter angegebene Datei die im S3-Storage noch nicht existieren darf.
     *
     * @param fileData mit der Datei und den Dateimetadaten zum initialen Speichern.
     * @throws FileExistanceException falls die Datei bereits existiert.
     * @throws BrokenFileException    falls aus der {@link FileData#getMultipartFile()} der Inpustream nicht lesbar.
     * @throws S3AccessException      falls nicht auf den S3-Storage zugegriffen werden kann.
     */
    public void saveFile(final FileData fileData) throws FileExistanceException, BrokenFileException, S3AccessException {
        final String pathToFolder = fileData.getRefId();
        final String pathToFile = FileHandlingService.createFilePath(
                pathToFolder,
                fileData.getMultipartFile().getOriginalFilename()
        );
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (filepathesInFolder.contains(pathToFile)) {
            throw new FileExistanceException("Die Datei existiert bereits.");
        } else {
            this.updateFile(fileData);
        }
    }

    /**
     * Uberschreibt die im Parameter angegebene Datei die im S3-Storage bereits existiert.
     * Des Weiteren wird in der Datenbank der Eintrag bezüglich {@link Folder#getEndOfLife()} angepasst.
     * <p>
     * Gibt es die Datei noch nicht im S3-Storage, so wird diese neu angelegt und in der Datenbank
     * ein entsprechender {@link Folder} persistiert.
     *
     * @param fileData mit der Datei und den Dateimetadaten zum erneuten Speichern.
     * @throws BrokenFileException falls aus der {@link FileData#getMultipartFile()} der Inpustream nicht lesbar.
     * @throws S3AccessException   falls nicht auf den S3-Storage zugegriffen werden kann.
     */
    public void updateFile(final FileData fileData) throws BrokenFileException, S3AccessException {
        final String pathToFile = FileHandlingService.createFilePath(
                fileData.getRefId(),
                fileData.getMultipartFile().getOriginalFilename()
        );
        final Optional<Folder> folderOptional = this.folderRepository.findByRefId(fileData.getRefId());
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
        try {
            this.s3Repository.uploadFile(
                    pathToFile,
                    fileData.getMultipartFile().getInputStream()
            );
        } catch (final IOException exception) {
            this.folderRepository.deleteByRefId(fileData.getRefId());
            throw new BrokenFileException("Die Datei konnte nicht hochgeladen werden.", exception);
        }
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
