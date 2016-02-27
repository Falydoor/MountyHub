package com.mountyhub.app.service.util;

import com.mountyhub.app.domain.enumeration.ScriptName;

/**
 * Created by Theo on 2/27/16.
 */
public final class MountyHallScriptUtil {

    public static String BASE_URL = "http://sp.mountyhall.com/";

    public static String getUrl(ScriptName name, Long number, String password) {
        return BASE_URL + name + ".php?Numero=" + number + "&Motdepasse=" + password;
    }
}
