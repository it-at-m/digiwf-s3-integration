package io.muenchendigital.digiwf.s3.integration.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.muenchendigital.digiwf.s3.client")
public class S3IntegrationClientProperties {

    @NotBlank
    private String documentStorageUrl;

}
