package io.muenchendigital.digiwf.s3.integration.configuration;

import io.minio.MinioClient;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import io.muenchendigital.digiwf.s3.integration.properties.S3IntegrationProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "io.muenchendigital.digiwf.s3.integration")
@EntityScan(basePackages = "io.muenchendigital.digiwf.s3.integration")
@ComponentScan(basePackages = "io.muenchendigital.digiwf.s3.integration")
@EnableConfigurationProperties(S3IntegrationProperties.class)
public class S3IntegrationAutoConfiguration {

    public final S3IntegrationProperties s3IntegrationProperties;

    @Bean
    public S3Repository s3Repository() throws S3AccessException {
        final MinioClient minioClient = MinioClient.builder()
                .endpoint(this.s3IntegrationProperties.getUrl())
                .credentials(this.s3IntegrationProperties.getAccessKey(), this.s3IntegrationProperties.getSecretKey())
                .build();
        return new S3Repository(
                this.s3IntegrationProperties.getBucketName(),
                minioClient,
                BooleanUtils.isNotFalse(this.s3IntegrationProperties.getInitialConnectionTest())
        );
    }
}
