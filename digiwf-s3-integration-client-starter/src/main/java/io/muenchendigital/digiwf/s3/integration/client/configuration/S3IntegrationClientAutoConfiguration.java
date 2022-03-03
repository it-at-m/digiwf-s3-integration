package io.muenchendigital.digiwf.s3.integration.client.configuration;

import io.muenchendigital.digiwf.s3.integration.client.gen.ApiClient;
import io.muenchendigital.digiwf.s3.integration.client.properties.S3IntegrationClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@EnableConfigurationProperties(S3IntegrationClientProperties.class)
public class S3IntegrationClientAutoConfiguration {

    public final S3IntegrationClientProperties s3IntegrationClientProperties;

    /**
     * Creates a bean with name "s3IntegrationApiClient" to avoid name collisions
     * regarding other beans of type {@link ApiClient}.
     *
     * @param restTemplate to create rest requests.
     *                     If the S3 integration service is secured via Oauth2,
     *                     the OAuth2RestTemplate can be used here, for example.
     * @return the client with correct base path.
     */
    @Bean
    public ApiClient s3IntegrationApiClient(final RestTemplate restTemplate) {
        final var formApiClient = new ApiClient(restTemplate);
        formApiClient.setBasePath(this.s3IntegrationClientProperties.getDocumentStorageUrl());
        return formApiClient;
    }

}
