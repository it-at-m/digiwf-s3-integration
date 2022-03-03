package io.muenchendigital.digiwf.s3.integration.client.configuration;

import io.muenchendigital.digiwf.s3.integration.gen.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    /**
     * Provides the {@link RestTemplate} which is used in {@link ApiClient}.
     *
     * @return the {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
