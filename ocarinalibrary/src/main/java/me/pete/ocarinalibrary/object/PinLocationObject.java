package me.pete.ocarinalibrary.object;

import com.google.android.gms.maps.model.LatLng;

import androidx.appcompat.app.AppCompatActivity;
import me.pete.ocarinalibrary.dialog.MaterialDialog;

public class PinLocationObject {
    private LatLng latLng;
    private String title;

    public PinLocationObject(LatLng latLng, String title) {
        this.latLng = latLng;
        this.title = title;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void dialog(AppCompatActivity activity) {
    }
}
