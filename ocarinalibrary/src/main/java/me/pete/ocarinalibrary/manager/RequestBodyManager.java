package me.pete.ocarinalibrary.manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import me.pete.ocarinalibrary.listener.OnRequestListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

public class RequestBodyManager extends RequestBody {
    private ForwardingSinkManager forwardingSinkManager;
    private RequestBody requestBody;

    private OnRequestListener onRequestListener;

    public RequestBodyManager(RequestBody requestBody, OnRequestListener onRequestListener) {
        this.requestBody = requestBody;
        this.onRequestListener = onRequestListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(@NotNull BufferedSink sink) throws IOException {
        forwardingSinkManager = new ForwardingSinkManager(sink, requestBody, onRequestListener);
        BufferedSink bufferedSink = Okio.buffer(forwardingSinkManager);

        requestBody.writeTo(bufferedSink);

        bufferedSink.flush();
    }
}
