package com.spring.azure.springazurecloud.utils;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class IdentityGenerator {
    public static String generateId(){
        return RandomStringUtils.randomAlphanumeric(Constants.IDENTITY_LENGTH);
    }

    public static String generateSubscriptionId(){
        return UUID.randomUUID().toString();
    }

}
