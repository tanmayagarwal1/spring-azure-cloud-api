package com.spring.azure.springazurecloud.enums;

public enum StorageAccessTier {
    HOT(10),COLD(5), ARCHIVE(1);

    private final long cost;
    StorageAccessTier(long cost){
        this.cost = cost;
    }

    public long getCost(){
        return this.cost;
    }

}