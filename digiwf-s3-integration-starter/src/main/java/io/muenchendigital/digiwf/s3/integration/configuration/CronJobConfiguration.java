package io.muenchendigital.digiwf.s3.integration.configuration;

import io.muenchendigital.digiwf.s3.integration.domain.service.S3AndDatabaseCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "io.muenchendigital.digiwf.s3.cronjob.cleanup",
        name = {
                "expired-folders",
                "database-folder-without-corresponding-s3-folder"
        }
)
public class CronJobConfiguration {

    private final S3AndDatabaseCleanupService s3AndDatabaseCleanupService;

    @Scheduled(cron = "${de.muenchen.documentstorage.cronjob.cleanup.expiredfolders}")
    public void cronJobDefinitionCleanUpExpiredFolders() {
        this.s3AndDatabaseCleanupService.cleanUpExpiredFolders();
    }

    @Scheduled(cron = "${de.muenchen.documentstorage.cronjob.cleanup.databasefolderwithoutcorrespondings3folder}")
    public void cronJobDefinitionCleanUpDatabaseFolderWithoutCorrespondingS3Folder() {
        this.s3AndDatabaseCleanupService.cleanUpDatabaseFolderWithoutCorrespondingS3Folder();
    }

}

