package io.muenchendigital.digiwf.s3.integration.domain.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class FileData {

    private MultipartFile multipartFile;

    private String refId;

    private LocalDate endOfLife;

}
