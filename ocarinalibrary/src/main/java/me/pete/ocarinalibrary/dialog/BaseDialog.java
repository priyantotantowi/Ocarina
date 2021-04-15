package me.pete.ocarinalibrary.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import me.pete.ocarinalibrary.R;
import me.pete.ocarinalibrary.enumerator.DialogTypeEnum;
import me.pete.ocarinalibrary.helper.ColorHelper;
import me.pete.ocarinalibrary.helper.CoordinateHelper;
import me.pete.ocarinalibrary.listener.OnGetCoordinateListener;
import me.pete.ocarinalibrary.object.PinLocationObject;

public class BaseDialog extends Dialog implements OnMapReadyCallback {
    private AppCompatActivity activity;
    private ArrayList<PinLocationObject> pinLocationObjects;
    private boolean isUsePolygon;
    private DialogTypeEnum dialogTypeEnum;
    private GoogleMap myGoogleMap;
    private LatLng latLng;
    private SupportMapFragment mapFragment;
    private TextView txtTitle, txtMessage, txtPositive, txtNegative, txtNeutral;

    public BaseDialog(@NonNull AppCompatActivity activity) {
        super(activity);
        this.activity = activity;
    }

    protected void setConfiguration(String titleText, String messageText, String positiveText, String negativeText, String neutralText,
                                    final me.pete.ocarinalibrary.listener.OnClickListener onPositiveClicked,
                                    final me.pete.ocarinalibrary.listener.OnClickListener onNegativeClicked,
                                    final me.pete.ocarinalibrary.listener.OnClickListener onNeutralClicked) {
        txtTitle = findViewById(R.id.txtTitle);
        txtMessage = findViewById(R.id.txtMessage);
        txtPositive = findViewById(R.id.txtPositive);
        txtNegative = findViewById(R.id.txtNegative);
        txtNeutral = findViewById(R.id.txtNeutral);

        txtPositive.setTextColor(ColorStateList.valueOf(ColorHelper.getAccentColorFromThemeIfAvailable(activity)));
        txtNegative.setTextColor(ColorStateList.valueOf(ColorHelper.getAccentColorFromThemeIfAvailable(activity)));
        txtNeutral.setTextColor(ColorStateList.valueOf(ColorHelper.getAccentColorFromThemeIfAvailable(activity)));

        if(titleText.contentEquals("")) {
            txtTitle.setText("Dialog");
        } else {
            txtTitle.setText(titleText);
        }

        if(titleText.contentEquals("")) {
            txtMessage.setText("Are you sure?");
        } else {
            txtMessage.setText(messageText);
        }

        if(positiveText.contentEquals("")) {
            txtPositive.setVisibility(View.GONE);
        } else {
            txtPositive.setText(positiveText);
        }

        if(neutralText.contentEquals("")) {
            txtNeutral.setVisibility(View.GONE);
        } else {
            txtNeutral.setText(neutralText);
        }

        txtPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPositiveClicked.onClick();
                dismiss();
            }
        });

        txtNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNegativeClicked.onClick();
                dismiss();
            }
        });

        txtNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNeutralClicked.onClick();
                dismiss();
            }
        });

        if(dialogTypeEnum == DialogTypeEnum.MAP) {
            mapFragment = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    protected void setUsePolygon(boolean usePolygon) {
        isUsePolygon = usePolygon;
    }

    protected void setView(DialogTypeEnum dialogTypeEnum) {
        this.dialogTypeEnum = dialogTypeEnum;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getView(dialogTypeEnum));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private int getView(DialogTypeEnum dialogTypeEnum) {
        switch (dialogTypeEnum) {
            case BASIC:
                return R.layout.dialog_basic;
            case MAP:
                return R.layout.dialog_map;
            default:
                return R.layout.dialog_basic;
        }
    }

    protected void setPinLocationObjects(PinLocationObject... pinLocations) {
        pinLocationObjects = new ArrayList<>();
        pinLocationObjects.addAll(Arrays.asList(pinLocations));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(dialogTypeEnum == DialogTypeEnum.MAP) {
            myGoogleMap = googleMap;
            myGoogleMap.clear();
            myGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            myGoogleMap.setTrafficEnabled(true);
            if(!pinLocationObjects.isEmpty()) {
                for (PinLocationObject pinLocationObject : pinLocationObjects) {
                    try {
                        new pinTask(myGoogleMap, pinLocationObject.getLatLng(), pinLocationObject.getTitle()).execute();
                    } catch (Exception e) {
                        Log.e("onMapReady", e.toString());
                    }
                }
            }

            new CoordinateHelper().getCoordinate(getContext(), new OnGetCoordinateListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onLocation(double latitude, double longitude) {
                    if(isUsePolygon) {
                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        latLngs.add(new LatLng(latitude, longitude));
                        for (PinLocationObject pinLocationObject : pinLocationObjects) {
                            PolylineOptions polylineOptions = new PolylineOptions();
                            polylineOptions.clickable(false);
                            polylineOptions.color(ColorHelper.getAccentColorFromThemeIfAvailable(activity));
                            polylineOptions.add(new LatLng(latitude, longitude));
                            polylineOptions.add(pinLocationObject.getLatLng());
                            Polyline polyline = myGoogleMap.addPolyline(polylineOptions);
                        }
                    }

                    latLng = new LatLng(latitude, longitude);
                    myGoogleMap.setMyLocationEnabled(true);
                    myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            });
        }
    }

    private class pinTask extends AsyncTask<String, Void, MarkerOptions> {
        private GoogleMap googleMap;
        private LatLng latLng;
        private String title;

        public pinTask(GoogleMap googleMap, LatLng latLng, String title) {
            this.googleMap = googleMap;
            this.latLng = latLng;
            this.title = title;
        }

        protected MarkerOptions doInBackground(String... urls) {
            return new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker_red_48px)).title(title);
        }

        protected void onPostExecute(MarkerOptions markerOptions) {
            this.googleMap.addMarker(markerOptions);
        }
    }
}
