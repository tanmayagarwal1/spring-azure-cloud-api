package com.spring.azure.springazurecloud.enums;

public enum ContainerRepositorySku {
    BASIC(1), STANDARD(3), PREMIUM(5);

    private long cost;

    ContainerRepositorySku(long cost) {
        this.cost = cost;
    }

    public long getCost(){
        return this.cost;
    }
}
