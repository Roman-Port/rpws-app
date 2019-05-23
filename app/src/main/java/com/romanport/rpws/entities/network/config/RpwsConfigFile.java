package com.romanport.rpws.entities.network.config;

import java.util.Dictionary;
import java.util.HashMap;

public class RpwsConfigFile {

    public String env;
    public Dictionary<String, String> apis;

    public String GetApiEndpoint(RpwsApiEndpoint type) {
        //Get base URL
        String b = apis.get(type.GetKey());

        //Overwrite custom params
        return b.replace("$$ENV$$", env);
    }
}
