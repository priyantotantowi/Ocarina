package me.pete.ocarinalibrary.manager;

import android.content.Context;

import java.io.File;

public class FileManager {
    private Context context;

    public FileManager(Context context) {
        this.context = context;
    }

    public ApplicationManager source(File file) {
        return new ApplicationManager(context, file);
    }

    public ApplicationManager source(String pathname) {
        return source(new File(pathname));
    }
}
