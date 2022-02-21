package io.muenchendigital.digiwf.s3.integration.configuration;

import io.muenchendigital.digiwf.s3.integration.domain.service.cronjob.CleanUpDatabaseFolderWithoutCorrespondingS3Folder;
import io.muenchendigital.digiwf.s3.integration.domain.service.cronjob.CleanUpExpiredFolders;
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

    private final CleanUpExpiredFolders cleanUpExpiredFolders;

    private final CleanUpDatabaseFolderWithoutCorrespondingS3Folder cleanUpDatabaseFolderWithoutCorrespondingS3Folder;

    @Scheduled(cron = "${de.muenchen.documentstorage.cronjob.cleanup.expired-folders}")
    public void cronJobDefinitionCleanUpExpiredFolders() {
        this.cleanUpExpiredFolders.cleanUp();
    }

    @Scheduled(cron = "${de.muenchen.documentstorage.cronjob.cleanup.database-folder-without-corresponding-s3-folder}")
    public void cronJobDefinitionCleanUpDatabaseFolderWithoutCorrespondingS3Folder() {
        this.cleanUpDatabaseFolderWithoutCorrespondingS3Folder.cleanUp();
    }

}

