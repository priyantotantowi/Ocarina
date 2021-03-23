package me.pete.ocarinalibrary.listener;

public interface OnCallbackListener {
    void onFailure(String message);
    void onResponse(String response);
}
