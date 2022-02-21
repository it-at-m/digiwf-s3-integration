package io.muenchendigital.digiwf.s3.integration.configuration;

import io.muenchendigital.digiwf.s3.integration.domain.service.cronjob.CleanUpDatabaseFolderWithoutCorrespondingS3Folder;
import io.muenchendigital.digiwf.s3.integration.domain.service.cronjob.CleanUpExpiredFolders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
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

    private final CleanUpExpiredFolders cleanUpExpiredFolders;

    private final CleanUpDatabaseFolderWithoutCorrespondingS3Folder cleanUpDatabaseFolderWithoutCorrespondingS3Folder;

    @Scheduled(cron = "${io.muenchendigital.digiwf.s3.cronjob.cleanup.expired-folders}")
    public void cronJobDefinitionCleanUpExpiredFolders() {
        this.cleanUpExpiredFolders.cleanUp();
    }

    @Scheduled(cron = "${io.muenchendigital.digiwf.s3.cronjob.cleanup.database-folder-without-corresponding-s3-folder}")
    public void cronJobDefinitionCleanUpDatabaseFolderWithoutCorrespondingS3Folder() {
        this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.cleanUp();
    }

}

