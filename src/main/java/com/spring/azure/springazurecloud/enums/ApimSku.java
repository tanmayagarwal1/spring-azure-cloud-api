package com.spring.azure.springazurecloud.enums;

public enum ApimSku {
    DEVELOPER(100), BASIC(500), STANDARD(1000), PREMIUM(1500), ISOLATED(5000);

    private long cost;

     ApimSku(long cost) {
        this.cost = cost;
    }

    public long getCost(){
         return this.cost;
    }
}
