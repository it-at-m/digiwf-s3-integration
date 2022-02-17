package io.muenchendigital.digiwf.s3.integration.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDto {

    private String presignedUrl;

}
