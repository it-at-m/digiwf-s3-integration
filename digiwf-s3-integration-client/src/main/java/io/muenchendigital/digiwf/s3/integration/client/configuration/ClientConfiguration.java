package io.muenchendigital.digiwf.s3.integration.client.configuration;

import io.muenchendigital.digiwf.s3.integration.client.gen.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        ApiClient.class
                }
        )
})
public class ClientConfiguration {

    @Value("${io.muenchendigital.digiwf.s3.client.url.integration.service}")
    private String documentStorageUrl;

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
        formApiClient.setBasePath(this.documentStorageUrl);
        return formApiClient;
    }

}
