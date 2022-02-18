package io.muenchendigital.digiwf.s3.integration.api.mapper;

import io.muenchendigital.digiwf.s3.integration.api.dto.FileResponseDto;
import io.muenchendigital.digiwf.s3.integration.configuration.MapstructConfiguration;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface FileResponseMapper {

    FileResponseDto model2Dto(final FileResponse fileResponse);

}
