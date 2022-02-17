package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AndDatabaseAsyncException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FolderHandlingService {

    private final S3Repository s3Repository;

    private final FolderRepository folderRepository;

    /**
     * Löscht den im Parameter angegebenen Ordner samt den dazugehörigen Datenbankeintrag.
     *
     * @param refId identifiziert den Namen des Ordners.
     * @throws S3AndDatabaseAsyncException falls im S3-Storage der Ordner vorhanden ist und in der Datenbank nicht und umgekehrt.
     * @throws S3AccessException           falls nicht auf den S3-Storage zugegriffen werden kann.
     */
    public void deleteFolder(final String refId) throws S3AndDatabaseAsyncException, S3AccessException {
        final String pathToFolder = refId;
        final Optional<Folder> folderOptional = this.folderRepository.findByRefId(refId);
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (folderOptional.isPresent() && !filepathesInFolder.isEmpty()) {
            // Delete all files on S3
            for (final String pathToFile : filepathesInFolder) {
                this.s3Repository.deleteFile(pathToFile);
            }
            // Delete database entry
            this.folderRepository.deleteByRefId(refId);
        } else if (folderOptional.isEmpty() && !filepathesInFolder.isEmpty()) {
            throw new S3AndDatabaseAsyncException(
                    "Keine Ordner-Entität in Datenbank jedoch Dateien in Ordner auf S3-Storage vorhanden für Reference-ID " + refId
            );
        } else if (folderOptional.isPresent() && filepathesInFolder.isEmpty()) {
            throw new S3AndDatabaseAsyncException(
                    "Kein Ordner auf S3-Storage jedoch Ordner-Entität in Datenbank vorhanden für Reference-ID " + refId
            );
        } else {
            // Ordner in S3 und in Ordner-Entität in Datenbank existiert nicht -> alles i.O.
        }
    }

}
