package io.muenchendigital.digiwf.s3.integration;

import io.muenchendigital.digiwf.s3.integration.client.configuration.S3IntegrationClientAutoConfiguration;
import io.muenchendigital.digiwf.s3.integration.client.gen.ApiClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


@SpringBootApplication
@ComponentScan(
        basePackages = "io.muenchendigital.digiwf.s3.integration",
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                /**
                                 * Exclude to avoid multiple instantiation of multiple beans with same name.
                                 * This class is instantiated in {@link S3IntegrationClientAutoConfiguration}
                                 * to give the bean another name.
                                 */
                                ApiClient.class
                        }
                )
        })
public class S3IntegrationClientApplication {

    public static void main(final String[] args) {
        SpringApplication.run(S3IntegrationClientApplication.class, args);
    }

}
