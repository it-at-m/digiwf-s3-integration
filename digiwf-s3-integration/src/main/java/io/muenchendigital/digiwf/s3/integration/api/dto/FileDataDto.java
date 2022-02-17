package io.muenchendigital.digiwf.s3.integration.api.dto;

import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class FileDataDto {

    @NotNull
    private MultipartFile multipartFile;

    @NotNull
    @Size(max = FolderRepository.LENGTH_REF_ID)
    private String refId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endOfLife;

}
