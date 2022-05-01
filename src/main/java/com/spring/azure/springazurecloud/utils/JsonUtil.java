package com.spring.azure.springazurecloud.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.exception.JsonConversionException;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@UtilityClass
public class JsonUtil {
    private static final ObjectWriter writer;
    private static final Gson gson;
    static {
        writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        gson = new Gson();
    }

    public static <T> Optional<String> toJson(T object){
        return ofNullable(object)
                .map(o -> {
                    try {
                        return writer.writeValueAsString(o);
                    } catch (JsonProcessingException e) {
                        return e.getMessage();
                    }
                });
    }

    public static <T> Optional<T> fromJson(String json, Class<T> clazz){
        return ofNullable(json)
                .map(j -> gson.fromJson(j, clazz));
    }

    public static Optional<String> jsonFromRequest(HttpServletRequest request){
        try{
            return ofNullable(request)
                    .map(req -> {
                        try {
                            return req.getReader().lines().reduce("", (acc, act)-> acc+act);
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new JsonConversionException("Request Conversion Error");
                        }
                    });
        }catch (RuntimeException e){
            LogHelper.logError("Exception", e);
            return Optional.of(Constants.PLACEHOLDER);
        }
    }
}
