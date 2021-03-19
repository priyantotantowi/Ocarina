package me.pete.ocarinalibrary.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Priyanto Tantowi.
 *
 * ConnectionHelper is helper to all about connection information
 */
public abstract class ConnectionHelper {
    /**
     * This function returns boolean value. True if your mobile data / Wi-Fi is available and
     * false your mobile data / Wi-Fi is not available.
     *
     * This function not detect you have internet connection. This function only
     * check your phone is mobile data / Wi-Fi is active.
     */
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
