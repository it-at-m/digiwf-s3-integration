package io.muenchendigital.digiwf.s3.integration.infrastructure.repository;

import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface FolderRepository extends PagingAndSortingRepository<Folder, UUID> {

    int LENGTH_REF_ID = 512;

    void deleteByRefId(final String refId);

    Optional<Folder> findByRefId(final String refId);

    Stream<Folder> findAllByEndOfLifeNotNullAndEndOfLifeBefore(final LocalDate date);

}
