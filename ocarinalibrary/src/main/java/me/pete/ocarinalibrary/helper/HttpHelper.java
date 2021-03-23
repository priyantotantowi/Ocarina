package me.pete.ocarinalibrary.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.pete.ocarinalibrary.enumerator.BodyPostTypeEnum;
import me.pete.ocarinalibrary.enumerator.RequestMethodEnum;
import me.pete.ocarinalibrary.listener.OnCallbackListener;
import me.pete.ocarinalibrary.listener.OnReachableListener;
import me.pete.ocarinalibrary.listener.OnRequestListener;
import me.pete.ocarinalibrary.manager.RequestBodyManager;
import me.pete.ocarinalibrary.object.HttpFormBodyObject;
import me.pete.ocarinalibrary.object.HttpHeaderObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * HttpHelper is helper for all about http library.
 *
 * HttpHelper is build on base OkHttp3. The developer enhancement
 * OkHttp for common http.
 */
public final class HttpHelper {
    private ArrayList<HttpHeaderObject> httpHeaderObjects = new ArrayList<>();
    private ArrayList<HttpFormBodyObject> httpFormBodyObjects = new ArrayList<>();
    private Call callupload;
    private int timeout = 0;
    private String url = "";
    private String httpRawBody = "";
   // private String response = "";
    Uri uri;

    private BodyPostTypeEnum bodyPostTypeEnum;

    /**
     * This is Constructor for HttpHelper
     * @param url   Your URL.
     */
    public HttpHelper(String url) {
        this.url = url;
    }

    /**
     * @return      call of Upload
     */
    public Call getCallupload() {
        return callupload;
    }

    /**
     * @return Your URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set timeout for http still alive for waiting response.
     *
     * @param timeout       Parameter timeout in seconds
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Set HttpHeaderObject.
     *
     * @param httpHeaderObjects
     */
    public void setHttpHeaderObjects(ArrayList<HttpHeaderObject> httpHeaderObjects) {
        this.httpHeaderObjects = httpHeaderObjects;
    }

    /**
     * Set HttpFormBody.
     *
     * @param httpFormBodyObjects
     */
    public void setHttpFormBodyObjects(ArrayList<HttpFormBodyObject> httpFormBodyObjects) {
        this.httpFormBodyObjects = httpFormBodyObjects;
    }

    /**
     * Set HttpRawBody, this function set raw body for http post.
     *
     * @param httpRawBody       Your raw body like json.
     */
    public void setHttpRawBody(String httpRawBody) {
        this.httpRawBody = httpRawBody;
    }

    /**
     * Set body post type.
     *
     * @param bodyPostTypeEnum      Available type is Form-Data and Raw
     */
    public void setBodyPostTypeEnum(BodyPostTypeEnum bodyPostTypeEnum) {
        this.bodyPostTypeEnum = bodyPostTypeEnum;
    }

    /**
     * This function used for connect to API and will be return by callback.
     * OnResponse means HttpHelper get a response from API and OnFailure means
     * HttpHelper can't found this API.
     *
     * This function used for background task.
     *
     * @param onCallbackListener    The Callback of function.
     */
    @SuppressLint("LongLogTag")
    public void connect(final OnCallbackListener onCallbackListener) {
        try {
            Log.i("HttpHelper-connect", "Start OkHttpClient " + getUrl());
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .callTimeout(timeout, TimeUnit.SECONDS)
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build();

            RequestBody requestBody;
            if (bodyPostTypeEnum == BodyPostTypeEnum.FORM_DATA) {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for (HttpFormBodyObject httpFormBodyObject : httpFormBodyObjects) {
                    if (httpFormBodyObject.getFile() != null) {
                        builder.addFormDataPart(httpFormBodyObject.getKey(), httpFormBodyObject.getFilename(), RequestBody.create(MediaType.parse(getMediaType(httpFormBodyObject.getFilename())), httpFormBodyObject.getFile()));
                    } else {
                        builder.addFormDataPart(httpFormBodyObject.getKey(), httpFormBodyObject.getValue());
                    }
                }
                requestBody = builder.build();
            } else {
                Log.i("httpRawBody", httpRawBody);
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), httpRawBody);
            }

            Request request = new Request.Builder()
                    .url(getUrl())
                    .post(requestBody)
                    .build();

            try {
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("HttpHelper-onFailure", e.toString());
                        try {
                            onCallbackListener.onFailure(e.toString());
                        } catch (Exception e1) {
                            Log.e("HttpHelper-onFailure", e1.toString());
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            final String res = response.body().string();
                            Log.i("HttpHelper-onResponse", res);
                            response.close();
                            onCallbackListener.onResponse(res);
                        } catch (Exception e) {
                            Log.i("HttpHelper-onResponse", e.toString());
                            onCallbackListener.onFailure(e.toString());
                        }
                    }
                });
                // Do something with the response.
            } catch (Exception e) {
                Log.e("HttpHelper-connect", e.toString());
                onCallbackListener.onFailure(e.toString());
            }
        } catch (Exception e) {
            Log.e("HttpHelper-connect", e.toString());
            onCallbackListener.onFailure(e.toString());
        }
    }

    /**
     * This function used for connect to API and will be return by callback.
     * OnResponse means HttpHelper get a response from API and OnFailure means
     * HttpHelper can't found this API.
     *
     * This function used for foreground task.
     *
     * @param onCallbackListener    The Callback of function.
     */
    public void execute(final OnCallbackListener onCallbackListener) {
        try {
            Log.i("HttpHelper-connect", "Start OkHttpClient " + getUrl());
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .callTimeout(timeout, TimeUnit.SECONDS)
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build();

            RequestBody requestBody;
            if (bodyPostTypeEnum == BodyPostTypeEnum.FORM_DATA) {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for (HttpFormBodyObject httpFormBodyObject : httpFormBodyObjects) {
                    if (httpFormBodyObject.getFile() != null) {
                        builder.addFormDataPart(httpFormBodyObject.getKey(), httpFormBodyObject.getFilename(), RequestBody.create(MediaType.parse(getMediaType(httpFormBodyObject.getFilename())), httpFormBodyObject.getFile()));
                    } else {
                        builder.addFormDataPart(httpFormBodyObject.getKey(), httpFormBodyObject.getValue());
                    }
                }
                requestBody = builder.build();
            } else {
                Log.i("httpRawBody", httpRawBody);
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), httpRawBody);
            }

            Request request = new Request.Builder()
                    .url(getUrl())
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                onCallbackListener.onResponse(response.body().toString());
            } catch (Exception e) {
                Log.e("HttpHelper-connect", e.toString());
                onCallbackListener.onFailure(e.toString());
            }
        } catch (Exception e) {
            Log.e("HttpHelper-connect", e.toString());
            onCallbackListener.onFailure(e.toString());
        }
    }

    /**
     * This function used for upload some file using API.
     *
     * @param activity              Your activity.
     * @param progressDialog        Put progress dialog for inform buffering process upload file.
     * @param onCallbackListener    The Callback of function.
     */
    public void upload(final Activity activity, final ProgressDialog progressDialog, final OnCallbackListener onCallbackListener) {
        try {
            RequestBody requestBody;
            if (bodyPostTypeEnum == BodyPostTypeEnum.FORM_DATA) {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for (HttpFormBodyObject httpFormBodyObject : httpFormBodyObjects) {
                    if (httpFormBodyObject.getFile() != null) {
                        final File fileupload = httpFormBodyObject.getFile();
                        Uri uris = Uri.fromFile(fileupload);
                        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uris.toString());
                        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
                        builder.addFormDataPart(httpFormBodyObject.getKey(), httpFormBodyObject.getFilename(), RequestBody.create(fileupload, MediaType.parse(mime)));
                    } else {
                        builder.addFormDataPart(httpFormBodyObject.getKey(), httpFormBodyObject.getValue());
                    }
                }
                requestBody = builder.build();
            } else {
                Log.i("httpRawBody", httpRawBody);
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), httpRawBody);
            }

            Request request = new Request.Builder()
                    .url(getUrl())
                    .post(requestBody)
                    .build();

            try {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request originalRequest = chain.request();
                                if (originalRequest.body() == null) {
                                    return chain.proceed(originalRequest);
                                }
                                Request progressRequest = originalRequest.newBuilder()
                                        .method(originalRequest.method(),
                                                new RequestBodyManager(originalRequest.body(), new OnRequestListener() {
                                                    @Override
                                                    public void onRequestProgress(long bytesWritten, long contentLength) {
                                                        if (contentLength > 0) {
                                                            final int progress = (int) (((double) bytesWritten / contentLength) * 100);
                                                            if (progressDialog != null)
                                                                activity.runOnUiThread(new Runnable() {
                                                                    public void run() {
                                                                        progressDialog.setProgress(progress);
                                                                    }
                                                                });

                                                            if (progress >= 100) {
                                                            }
                                                            Log.e("uploadProgress", contentLength + " ");
                                                        }
                                                    }
                                                }))
                                        .build();

                                return chain.proceed(progressRequest);
                            }
                        })
                        .build();

                callupload = okHttpClient.newCall(request);
                callupload.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("HttpHelper-onFailure", e.toString());
                        try {
                            onCallbackListener.onFailure(e.toString());
                        } catch (Exception e1) {
                            Log.e("HttpHelper-onFailure", e1.toString());
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            final String res = response.body().string();
                            Log.i("HttpHelper-onResponse", res);
                            response.close();
                            onCallbackListener.onResponse(res);
                        } catch (Exception e) {
                            Log.i("HttpHelper-onResponse", e.toString());
                        }
                    }
                });


            } catch (Exception e) {
                Log.e("HttpHelper-connect", e.toString());
                onCallbackListener.onFailure(e.toString());
            }
        } catch (Exception e) {
            Log.e("HttpHelper-connect", e.toString());
            onCallbackListener.onFailure(e.toString());
        }
    }

    /**
     * This class handling download file from API. You cant using method request
     * Post or Get to download file.
     */
    public class Download {
        private boolean isProgressShow = false;
        private Context context;
        private int progressStyle;
        private OnCallbackListener onCallbackListener;
        private ProgressDialog progressDialog;
        private RequestMethodEnum requestMethodEnum;
        private String filename, path, url, message = "", rawbody = "";

        public Download(Context context, String url, RequestMethodEnum requestMethodEnum, String path, String filename, boolean isProgressShow, int progressStyle, OnCallbackListener onCallbackListener) {
            this.onCallbackListener = onCallbackListener;
            this.context = context;
            this.filename = filename;
            this.path = path;
            this.url = url;
            this.requestMethodEnum = requestMethodEnum;
            this.isProgressShow = isProgressShow;
            this.progressStyle = progressStyle;
            new onDownloadTask().execute();
        }

        public Download(Context context, String url, RequestMethodEnum requestMethodEnum, String rawbody, String path, String filename, boolean isProgressShow, int progressStyle, OnCallbackListener onCallbackListener) {
            this.onCallbackListener = onCallbackListener;
            this.context = context;
            this.filename = filename;
            this.path = path;
            this.url = url;
            this.requestMethodEnum = requestMethodEnum;
            this.isProgressShow = isProgressShow;
            this.progressStyle = progressStyle;
            this.rawbody = rawbody;
            new onDownloadTask().execute();
        }

        private class onDownloadTask extends AsyncTask<String, String, String> {
            private boolean isFileExistOnServer;
            private HttpURLConnection httpURLConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isFileExistOnServer = true;
                if (isProgressShow) {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Downloading...");
                    progressDialog.setProgressStyle(progressStyle);
                    progressDialog.setCancelable(false);
                    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            httpURLConnection.disconnect();
                            if (new File(path + filename).exists()) {
                                new File(path + filename).delete();
                            }
                            onCallbackListener.onFailure(message);
                            dialog.dismiss();
                        }
                    });
                    progressDialog.show();

                    File file = new File(path + filename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            protected String doInBackground(String... strings) {
                int count = 0;
                try {
                    URL urlConnection = new URL(url);
                    httpURLConnection = (HttpURLConnection) urlConnection.openConnection();
                    Log.i("HttpHelper-Url", httpURLConnection.getURL().toString());
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    if (requestMethodEnum == RequestMethodEnum.GET) {
                        httpURLConnection.setRequestMethod("GET");
                    } else {
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                        outputStreamWriter.write(rawbody);
                        outputStreamWriter.close();
                    }
                    httpURLConnection.connect();

                    String contentType = "";
                    int fileLength = 0;
                    try {
                        fileLength = Integer.parseInt(httpURLConnection.getHeaderField("Content-Length"));
                    } catch (Exception e) {
                        fileLength = 0;
                    }

                    try {
                        contentType = httpURLConnection.getHeaderField("Content-Type");
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    Log.i("HttpHelper", contentType);
                    if (contentType != null) {
                        File file = new File(path);
                        File fileCek = new File(path + filename);

                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        if (fileCek.exists()) {
                            fileCek.delete();
                        }

                        File outputFile = new File(file, filename);
                        FileOutputStream fos = new FileOutputStream(outputFile);
                        InputStream inputStream = httpURLConnection.getInputStream();

                        byte[] buffer = new byte[1024];
                        long total = 0;
                        while ((count = inputStream.read(buffer)) != -1) {
                            total += count;
                            fos.write(buffer, 0, count);
                            if (fileLength > 0) {
                                publishProgress("" + (int) (total * 100 / fileLength));
                            }
                        }

                        fos.flush();
                        fos.close();
                        inputStream.close();
                    }
                } catch (FileNotFoundException e) {
                    Log.e("HttpHelper-onDownloadTask", e.toString());
                    isFileExistOnServer = false;
                    message = e.toString();
                } catch (Exception e) {
                    Log.e("HttpHelper-onDownloadTask", e.toString());
                    message = e.toString();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                if (isProgressShow) {
                    progressDialog.setProgress(Integer.parseInt(values[0]));
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (isProgressShow) {
                    progressDialog.dismiss();
                }
                File file = new File(path + filename);
                if (file.exists()) {
                    if (file.length() > 0) {
                        onCallbackListener.onResponse(message);
                    } else {
                        file.delete();
                        if (isFileExistOnServer) {
                            onCallbackListener.onFailure(message);
                        } else {
                            onCallbackListener.onResponse(message);
                        }
                    }
                } else {
                    if (isFileExistOnServer) {
                        onCallbackListener.onFailure(message);
                    } else {
                        onCallbackListener.onResponse(message);
                    }
                }
            }
        }
    }

    /**
     * This function for checking your server API still Alive.
     *
     * @param url                   Your API Url.
     * @param timeout               Parameter for how long this request alive.
     * @param reachableListener     Callback of function.
     */
    public static void isReachable(String url, int timeout, final OnReachableListener reachableListener) {
        Log.i("HttpHelper-isReachable", "Start OkHttpClient " + url);
        try {
            final OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(timeout, TimeUnit.SECONDS)
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("HttpHelper-onFailure", e.toString());
                    reachableListener.onUnavailableAccess();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        final int code = response.code();
                        if (code == 200) {
                            reachableListener.onInternetAccess();
                        } else {
                            reachableListener.onUnavailableAccess();
                        }
                    } catch (Exception e) {
                        reachableListener.onUnavailableAccess();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            reachableListener.onUnavailableAccess();
        }
    }

    private String getMediaType(String filename) {
        if (filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "images/*";
        } else if (filename.endsWith(".zip")) {
            return "application/zip";
        } else {
            return "";
        }
    }

    /**
     * This function using for encode a text to url style.
     *
     * @param url       Your Url.
     * @return
     */
    public static String urlEncoding(String url) {
        String urlEncoding = "";
        for (int i = 0; i < url.length(); i++) {
            try {
                if (url.substring(i, i + 1).contentEquals(" ")) {
                    urlEncoding += url.substring(i, i + 1).replace(" ", "%20");
                } else if (url.substring(i, i + 1).contentEquals("!")) {
                    urlEncoding += url.substring(i, i + 1).replace(" ", "%21");
                } else if (url.substring(i, i + 1).contentEquals("\"")) {
                    urlEncoding += url.substring(i, i + 1).replace("\"", "%22");
                } else if (url.substring(i, i + 1).contentEquals("#")) {
                    urlEncoding += url.substring(i, i + 1).replace("#", "%23");
                } else if (url.substring(i, i + 1).contentEquals("$")) {
                    urlEncoding += url.substring(i, i + 1).replace("$", "%24");
                } else if (url.substring(i, i + 1).contentEquals("%")) {
                    urlEncoding += url.substring(i, i + 1).replace("%", "%25");
                } else if (url.substring(i, i + 1).contentEquals("&")) {
                    urlEncoding += url.substring(i, i + 1).replace("&", "%26");
                } else if (url.substring(i, i + 1).contentEquals("\'")) {
                    urlEncoding += url.substring(i, i + 1).replace("\'", "%27");
                } else if (url.substring(i, i + 1).contentEquals("(")) {
                    urlEncoding += url.substring(i, i + 1).replace("(", "%28");
                } else if (url.substring(i, i + 1).contentEquals(")")) {
                    urlEncoding += url.substring(i, i + 1).replace(")", "%29");
                } else if (url.substring(i, i + 1).contentEquals("*")) {
                    urlEncoding += url.substring(i, i + 1).replace("*", "%2A");
                } else if (url.substring(i, i + 1).contentEquals("+")) {
                    urlEncoding += url.substring(i, i + 1).replace("+", "%2B");
                } else if (url.substring(i, i + 1).contentEquals(",")) {
                    urlEncoding += url.substring(i, i + 1).replace(",", "%2C");
                } else {
                    urlEncoding += url.substring(i, i + 1);
                }
            } catch (Exception e) {
                Log.i("HttpHelper-Error", e.toString());
            }
        }
        return urlEncoding;
    }

    private void setTrustAllCertificate() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {

        }
    }
}
