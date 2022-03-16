package io.muenchendigital.digiwf.s3.integration.configuration;

import io.muenchendigital.digiwf.s3.integration.domain.service.cronjob.CleanUpDatabaseFilesWithoutCorrespondingS3Folder;
import io.muenchendigital.digiwf.s3.integration.domain.service.cronjob.CleanUpExpiredFiles;
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
                "unused-folders"
        }
)
public class CronJobConfiguration {

    private final CleanUpExpiredFiles cleanUpExpiredFiles;

    private final CleanUpDatabaseFilesWithoutCorrespondingS3Folder cleanUpDatabaseFolderWithoutCorrespondingS3Folder;

    @Scheduled(cron = "${io.muenchendigital.digiwf.s3.cronjob.cleanup.expired-folders}")
    public void cronJobDefinitionCleanUpExpiredFolders() {
        this.cleanUpExpiredFiles.cleanUp();
    }

    @Scheduled(cron = "${io.muenchendigital.digiwf.s3.cronjob.cleanup.unused-folders}")
    public void cronJobCleanUpUnusedFolders() {
        this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.cleanUp();
    }

}

