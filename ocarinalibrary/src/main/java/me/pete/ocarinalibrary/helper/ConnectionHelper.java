package me.pete.ocarinalibrary.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionHelper {
    public static boolean isConnected(Context ctx){
        ConnectivityManager connectivityManager =  (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean ret = true;
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (!networkInfo.isConnected()) {
                    ret = false;
                }
                if (!networkInfo.isAvailable()) {
                    ret = false;
                }
            }

            if (networkInfo == null) {
                ret = false;
            }
        } else {
            ret = false;
        }
        return ret;
    }
}
