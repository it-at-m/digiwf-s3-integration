package io.muenchendigital.digiwf.s3.integration.domain.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BrokenFileException extends Exception {

    public BrokenFileException(final String message, final Exception exception) {
        super(message, exception);
    }

    public BrokenFileException(final String message) {
        super(message);
    }

}

