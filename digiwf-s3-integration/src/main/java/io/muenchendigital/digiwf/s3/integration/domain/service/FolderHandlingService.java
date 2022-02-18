package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AndDatabaseAsyncException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Slf4j
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
    @Transactional
    public void deleteFolder(final String refId) throws S3AndDatabaseAsyncException, S3AccessException {
        final String pathToFolder = refId;
        final Optional<Folder> folderOptional = this.folderRepository.findByRefId(refId);
        final Set<String> filepathesInFolder = this.s3Repository.getFilepathesFromFolder(pathToFolder);
        if (folderOptional.isPresent() && !filepathesInFolder.isEmpty()) {
            // Delete all files on S3
            log.info("All ${} files in folder ${} will be deleted.", filepathesInFolder.size(), pathToFolder);
            for (final String pathToFile : filepathesInFolder) {
                this.s3Repository.deleteFile(pathToFile);
            }
            // Delete database entry
            log.info("The database entry for folder ${} will be deleted.", pathToFolder);
            this.folderRepository.deleteByRefId(refId);
        } else if (folderOptional.isEmpty() && !filepathesInFolder.isEmpty()) {
            final String message = "No folder entity in database but files in folder on S3 storage exist for reference ID " + refId;
            log.error(message);
            throw new S3AndDatabaseAsyncException(message);
        } else if (folderOptional.isPresent() && filepathesInFolder.isEmpty()) {
            final String message = "No folder on S3 storage but folder entity exists in database for reference ID " + refId;
            log.error(message);
            throw new S3AndDatabaseAsyncException(message);
        } else {
            log.info("Folder in S3 and folder entity in database does not exist -> everything ok.");
        }
    }

}
