package io.muenchendigital.digiwf.s3.integration.configuration;

import io.muenchendigital.digiwf.s3.integration.domain.service.S3AndDatabaseCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "de.muenchen.documentstorage", name = "cleanupcronjob")
public class CleanUpJobConfiguration {

    private final S3AndDatabaseCleanupService s3AndDatabaseCleanupService;

    @Scheduled(cron = "${de.muenchen.documentstorage.cleanupcronjob}")
    public void cronJobDefinition() {
        this.s3AndDatabaseCleanupService.cleanUp();
    }

}

