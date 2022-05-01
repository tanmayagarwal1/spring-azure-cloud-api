package com.spring.azure.springazurecloud.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class ResponseDto {
    private boolean success;
    private final HashMap<String, String> message = new HashMap<>();

    public ResponseDto(boolean success) {
        this.success = success;
    }

    public void addMessage(String key, String val){
        this.message.put(key, val);
    }
}
