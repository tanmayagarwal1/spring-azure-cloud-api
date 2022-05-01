package com.spring.azure.springazurecloud.utils;

import com.spring.azure.springazurecloud.enums.aks.AksSku;

import javax.persistence.AttributeConverter;

public class AksEnumConverter implements AttributeConverter<AksSku, String> {
    @Override
    public String convertToDatabaseColumn(AksSku aksSku) {
        if(aksSku == null) return null;
        return aksSku.toString();
    }

    @Override
    public AksSku convertToEntityAttribute(String s) {
        return null;
    }
}
