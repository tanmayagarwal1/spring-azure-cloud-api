package com.spring.azure.springazurecloud.dto.aks;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AksRequestDto {
    private String aksId;
    private String resourceId;
}
