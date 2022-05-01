package com.spring.azure.springazurecloud.utils;

import com.spring.azure.springazurecloud.exception.acr.ContainerRegistryException;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@UtilityClass
public class Sha256 {

    public static String getHash(String imageName){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(imageName.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);

        }catch(RuntimeException | NoSuchAlgorithmException e){
            LogHelper.logError("Error has occurred in generating Hash", e);
            throw new ContainerRegistryException("Error has Occurred while generating Hash");
        }
    }
}
