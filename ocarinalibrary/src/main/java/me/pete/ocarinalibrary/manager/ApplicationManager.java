package me.pete.ocarinalibrary.manager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public final class ApplicationManager {
    private boolean canDelete;
    private Context context;
    private File file;
    private Class pdfViewerActivity;
    private Class photoViewerActivity;
    private Class videoPlayerActivity;

    public ApplicationManager(Context context, File file) {
        this.context = context;
        this.file = file;
    }

    public ApplicationManager canDelete(boolean canDelete) {
        this.canDelete = canDelete;
        return this;
    }

    public ApplicationManager pdfViewer(Class pdfViewerActivity) {
        this.pdfViewerActivity = pdfViewerActivity;
        return this;
    }

    public ApplicationManager photoViewer(Class photoViewerActivity) {
        this.photoViewerActivity = photoViewerActivity;
        return this;
    }

    public ApplicationManager videoPlayer(Class videoPlayerActivity) {
        this.videoPlayerActivity = videoPlayerActivity;
        return this;
    }

    public void open() {
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        String path = FilenameUtils.getPath(file.getPath());
        String filename = FilenameUtils.getName(file.getPath());

        if(path.contentEquals("")) {
            return;
        }

        if(filename.contentEquals("")) {
            return;
        }

        if(filename.toLowerCase().endsWith(".pdf")) {
            if(pdfViewerActivity != null) {
                openPDF(context, path, filename);
            } else {
                openDefaultPDF(context, uri);
            }
        } else if(filename.toLowerCase().endsWith(".docx") || filename.toLowerCase().endsWith(".doc")) {
            openDefaultMSWord(context, uri);
        } else if(filename.toLowerCase().endsWith(".xls") || filename.toLowerCase().endsWith(".xlsx")) {
            openDefaultMSExcel(context, uri);
        } else if(filename.toLowerCase().endsWith("mp4")) {
            if(videoPlayerActivity != null) {
                openVideo(context, path, filename);
            } else {
                openDefaultVideo(context, uri);
            }
        } else if(filename.toLowerCase().endsWith(".jpg") ||
                filename.toLowerCase().endsWith(".jpeg") ||
                filename.toLowerCase().endsWith(".png")) {
            if(photoViewerActivity != null) {
                openImage(context, path, filename, canDelete);
            } else {
                openDefaultImage(context, uri);
            }
        }
    }

    private void openDefaultImage(Context context, Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        context.startActivity(intent);
    }

    private void openDefaultPDF(Context context, Uri uri) {
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(uri,"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    private void openDefaultMSWord(Context context, Uri uri) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW);
        myIntent.setDataAndType(uri,"application/msword");
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = Intent.createChooser(myIntent, "Open File");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    private void openDefaultMSExcel(Context context, Uri uri) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW);
        myIntent.setDataAndType(uri,"application/vnd.ms-excel");
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = Intent.createChooser(myIntent, "Open File");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    private void openDefaultVideo(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/mp4");
        context.startActivity(intent);
    }

    private void openPDF(Context context, String path, String filename) {
        Intent intent = new Intent(context, photoViewerActivity);
        intent.putExtra("path", path);
        intent.putExtra("filename", filename);
        context.startActivity(intent);
    }

    private void openVideo(Context context, String path, String filename) {
        Intent intent = new Intent(context, videoPlayerActivity);
        intent.putExtra("path", path);
        intent.putExtra("filename", filename);
        context.startActivity(intent);
    }

    private void openImage(Context context, String path, String filename, boolean canDelete) {
        Intent intent = new Intent(context, photoViewerActivity);
        intent.putExtra("path", path);
        intent.putExtra("fileName", filename);
        intent.putExtra("canDelete", canDelete);
        context.startActivity(intent);
    }
}
