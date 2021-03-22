package me.pete.ocarinalibrary.helper;

import android.os.Environment;

import java.io.File;

/**
 * Created by Priyanto Tantowi.
 *
 * FolderHelper is helper to all about folder.
 */
public final class FolderHelper {
    /**
     * This function for create a folder
     * @param path          The path your folder location.
     * @param folderName    The your folder name.
     */
    public static void create(String path, String folderName){
        File file = new File(Environment.getExternalStorageDirectory().toString() + path + folderName);
        if (!file.exists()){
            file.mkdir();
        }
    }
}
