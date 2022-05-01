package com.spring.azure.springazurecloud.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApimRequestDto {
    private String apimId;
    private String resourceId;
}
