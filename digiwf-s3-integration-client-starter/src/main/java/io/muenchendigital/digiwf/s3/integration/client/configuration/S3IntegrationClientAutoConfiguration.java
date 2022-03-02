package io.muenchendigital.digiwf.s3.integration.client.configuration;

import io.muenchendigital.digiwf.s3.integration.client.properties.S3IntegrationClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@RequiredArgsConstructor
@EntityScan(basePackages = "io.muenchendigital.digiwf.s3.integration.client")
@ComponentScan(basePackages = "io.muenchendigital.digiwf.s3.integration.client")
@EnableConfigurationProperties(S3IntegrationClientProperties.class)
public class S3IntegrationClientAutoConfiguration {

    public final S3IntegrationClientProperties s3IntegrationClientProperties;

    @Bean
    public ApiClient s3Repository() {

    }
}
