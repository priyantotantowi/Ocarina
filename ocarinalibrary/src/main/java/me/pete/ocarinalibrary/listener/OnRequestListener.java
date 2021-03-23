package me.pete.ocarinalibrary.listener;

public interface OnRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength);
}
