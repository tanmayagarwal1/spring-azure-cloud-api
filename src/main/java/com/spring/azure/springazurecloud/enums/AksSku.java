package com.spring.azure.springazurecloud.enums;

public enum AksSku {
    BASIC(100), STANDARD(500), PREMIUM(1000);

    private final long cost;

    AksSku(long cost){
        this.cost = cost;
    }

    public long getCost(){
        return this.cost;
    }

}
