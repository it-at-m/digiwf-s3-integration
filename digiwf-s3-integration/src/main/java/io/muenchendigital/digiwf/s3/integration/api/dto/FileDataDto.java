package io.muenchendigital.digiwf.s3.integration.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.muenchendigital.digiwf.s3.integration.domain.service.FileHandlingService;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class FileDataDto {

    @NotEmpty
    @Size(max = FolderRepository.LENGTH_REF_ID)
    private String refId;

    @NotEmpty
    private String filename;

    /**
     * Definition des Gültigkeitszeitraums der Presigned-URL.
     */
    @NotNull
    @Min(FileHandlingService.MIN_EXPIRES_IN_MINUTES)
    private Integer expiresInMinutes;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endOfLife;

}
