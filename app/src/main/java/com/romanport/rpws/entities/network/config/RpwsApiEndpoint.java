package com.romanport.rpws.entities.network.config;

public enum RpwsApiEndpoint {

    ACCOUNT_INFO("account_info");

    private String key;
    private RpwsApiEndpoint(String k) {
        this.key = k;
    }

    public String GetKey() {
        return this.key;
    }
}
