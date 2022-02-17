package io.muenchendigital.digiwf.s3.integration.api.dto;

import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class FileDataDto {

    @NotEmpty
    @Size(max = FolderRepository.LENGTH_REF_ID)
    private String refId;

    @NotEmpty
    private String filename;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endOfLife;

}
