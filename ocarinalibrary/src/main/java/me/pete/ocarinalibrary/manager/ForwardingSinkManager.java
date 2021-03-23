package me.pete.ocarinalibrary.manager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import me.pete.ocarinalibrary.listener.OnRequestListener;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

public class ForwardingSinkManager extends ForwardingSink {
    private long bytesWritten = 0;
    private RequestBody requestBody;

    private OnRequestListener onRequestListener;

    public ForwardingSinkManager(@NotNull Sink delegate) {
        super(delegate);
    }

    public ForwardingSinkManager(@NotNull Sink delegate, RequestBody requestBody, OnRequestListener onRequestListener) {
        super(delegate);
        this.requestBody = requestBody;
        this.onRequestListener = onRequestListener;
    }

    @Override
    public void write(Buffer source, long byteCount) throws IOException {
        super.write(source, byteCount);

        bytesWritten += byteCount;
        onRequestListener.onRequestProgress(bytesWritten, requestBody.contentLength());
    }
}
