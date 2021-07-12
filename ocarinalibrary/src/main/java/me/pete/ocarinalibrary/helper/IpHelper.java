package com.eskalink.eskamobile.Helper;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class IpHelper {
    public static  String getIPPublic (){
        String result = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL myIp = new URL("https://checkip.amazonaws.com/");
            URLConnection c = myIp.openConnection();
            c.setConnectTimeout(1000);
            c.setReadTimeout(1000);
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            result = in.readLine();
        }catch (Exception e){

        }
        return result;
    }
}
