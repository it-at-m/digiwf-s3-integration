package io.muenchendigital.digiwf.s3.integration.api.mapper;

import io.muenchendigital.digiwf.s3.integration.api.dto.PresignedUrlDto;
import io.muenchendigital.digiwf.s3.integration.configuration.MapstructConfiguration;
import io.muenchendigital.digiwf.s3.integration.domain.model.PresignedUrl;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface FileResponseMapper {

    PresignedUrlDto model2Dto(final PresignedUrl presignedUrl);

}
