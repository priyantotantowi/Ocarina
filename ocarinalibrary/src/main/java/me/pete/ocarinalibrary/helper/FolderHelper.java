package me.pete.ocarinalibrary.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * Created by Priyanto Tantowi.
 *
 * FolderHelper is helper to all about folder.
 */
public final class FolderHelper {
    /**
     * Tests whether the file denoted by this abstract pathname is a
     * directory.
     *
     * @param path      Path of the directory.
     * @return
     */
    public static boolean isDirectory(String path){
        File file = new File(path);
        return file.isDirectory();
    }

    /**
     * This function for create a folder
     * @param path          The path your folder location.
     * @param folderName    The your folder name.
     */
    public static void create(Context context, String path, String folderName){
        int targetSdkVersion = 0;
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (targetSdkVersion >= Build.VERSION_CODES.Q) {
            File file = new File(context.getExternalFilesDir(null).getAbsolutePath() + path + folderName);
            if (!file.exists()) {
                file.mkdir();
            }
        } else {
            File file = new File(Environment.getExternalStorageDirectory().toString() + path + folderName);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }
}
