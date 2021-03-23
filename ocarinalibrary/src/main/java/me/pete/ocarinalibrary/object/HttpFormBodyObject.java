package me.pete.ocarinalibrary.object;

import java.io.File;

public class HttpFormBodyObject {
    private String key = "";
    private String value = "";
    private String filename = "";
    private File file = null;

    public HttpFormBodyObject(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public HttpFormBodyObject(String key, String filename, File file) {
        this.key = key;
        this.filename = filename;
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
