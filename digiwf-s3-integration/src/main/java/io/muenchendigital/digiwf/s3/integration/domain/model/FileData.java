package io.muenchendigital.digiwf.s3.integration.domain.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FileData {

    private String refId;

    private String filename;

    private LocalDate endOfLife;

}
