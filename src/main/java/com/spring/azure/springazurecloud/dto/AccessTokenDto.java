package com.spring.azure.springazurecloud.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccessTokenDto extends ResponseDto{
    private String access_token;
    private String refresh_token;
    private long validity_in_ms;

    @Builder
    public AccessTokenDto(boolean success, String access_token, String refresh_token, long validity_in_ms) {
        super(success);
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.validity_in_ms = validity_in_ms;
    }
}
