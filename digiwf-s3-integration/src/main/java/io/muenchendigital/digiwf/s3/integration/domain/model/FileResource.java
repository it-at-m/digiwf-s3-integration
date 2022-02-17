package io.muenchendigital.digiwf.s3.integration.domain.model;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class FileResource {

    private String filename;

    private Resource resource;

}
