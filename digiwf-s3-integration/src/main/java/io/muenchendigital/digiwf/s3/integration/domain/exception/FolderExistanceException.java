package io.muenchendigital.digiwf.s3.integration.domain.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FolderExistanceException extends Exception {

    public FolderExistanceException(final String message, final Exception exception) {
        super(message, exception);
    }

    public FolderExistanceException(final String message) {
        super(message);
    }

}

