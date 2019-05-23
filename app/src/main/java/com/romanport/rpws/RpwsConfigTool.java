package com.romanport.rpws;

import android.content.Context;

import com.romanport.rpws.entities.network.config.RpwsConfigFile;

import java.io.File;

public class RpwsConfigTool {

    private static RpwsConfigFile openedConfig;

    /**
     * Pulls the saved config file from the local file
     * @return
     */
    public static RpwsConfigFile GetCachedConfig(Context c) {
        //If we have the config opened and in memory, use it
        if(openedConfig != null)
            return openedConfig;

        //If we have it downloaded, load from the local storage.
        File file = new File(c.getFilesDir(), "saved_config.json");
        if(file.exists()) {

        }
        return null;
    }

}
