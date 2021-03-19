package me.pete.ocarinalibrary.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Created by Priyanto Tantowi.
 *
 * PhoneHelper is helper to all about phone info like
 * Android OS Version, Imei Number, Brand Name, Model Name
 * and etc.
 */

public abstract class PhoneHelper {
    private static final int CALL_PHONE = 1;

    private static Activity activity;
    private static String phoneNumber;

    /**
     * This function returns Android OS number (ex: 4.1.2, 5.0, 5.1, etc)
     */
    public static String getAndroidOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * This function returns Android OS name (ex: Jelly Bean, Lollipop, etc)
     */
    public static String getAndroidOSVersionName() {
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                return fieldName;
            }
        }
        return "";
    }

    /**
     * This function returns your smartphone brand (ex: Google, Samsung, Huawei, etc)
     */
    public static String getBrand() {
        return Build.MANUFACTURER;
    }

    /**
     * This function returns your smartphone model (ex: GD1YQ, SM-G980, ANA-AN00, etc)
     */
    public static String getModel() {
        // TODO: Get Model Device
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return StringHelper.toCapitalize(model);
        } else {
            return StringHelper.toCapitalize(manufacturer) + " " + model;
        }
    }

    /**
     * This function returns percentage your battery level.
     */
    public static int getBatteryLevel(Context context) {
        // TODO: Get percentage battery level now
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return intent.getIntExtra("level", -1);
    }

    /**
     * This function returns IMEI number for Android OS Version 10 (Android Q) or below and
     * return Device Id for Android OS 11 or Above.
     */
    @SuppressLint({"MissingPermission"})
    public static String getIMEI(Context context) {
        String imei = "";

        if (Build.VERSION.SDK_INT >= 29) {
            // TODO: If Android OS above or equal 29 (Android 10 / Android Q)
            imei = getDeviceId(context);
        } else {
            // TODO: If Android OS below 29 (Android 10 / Android Q)
            try {
                TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                imei = telephonyManager.getDeviceId();

                if(imei.contentEquals("00000000")) {
                    imei = getDeviceId(context);
                }
            } catch (Exception e) {
                // TODO: handle exception
                imei = getDeviceId(context);
            }
        }

        return imei;
    }

    /**
     * This function returns Device Id your phone. This number is unique.
     */
    public static String getDeviceId(Context context) {
        // TODO: Get Device ID from Android OS
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * This function returns boolean value. True for GPS is active and false for GPS is not active
     */
    public static boolean isGPSActive(Context context) {
        String locationProviders;
        int locationMode = 0;

        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }

        // TODO: If locationMode greater than 0 then active else not active
        if(locationMode > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function returns mobile data status in boolean value.
     * True if mobile data is active and false if mobile data is not active.
     */
    public static boolean isMobileDataActive(Context context) {
        // TODO: Return true if mobile data is Active
        return ConnectionHelper.isConnected(context);
    }

    /**
     * This function returns auto time and auto time zone status.
     * True if auto time and auto time zone is active and
     * false if not active.
     */
    public static boolean isAutoTimeAndAutoTimeZone(Context context) {
        // TODO: If your device not set automatic time zone
        String timeSettings = android.provider.Settings.System.getString(
                context.getContentResolver(),
                android.provider.Settings.System.AUTO_TIME);

        if(timeSettings.contentEquals("0")){
            return false;
        } else {
            timeSettings = android.provider.Settings.System.getString(
                    context.getContentResolver(),
                    android.provider.Settings.System.AUTO_TIME_ZONE);

            // TODO: Automatic time zone is disable
            // TODO: Automatic time zone is enable
            return !timeSettings.contentEquals("0");
        }
    }

    /**
     * This function returns auto time and auto time zone status.
     * True if developer option is active and false if not active.
     */
    public static boolean isDeveloperModeActive(Context context) {
        // TODO : If your device not active Developer Option
        int adb = Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED , 0);

        // TODO: Developer Option is enable
        // TODO: Developer Option is disable
        return adb == 1;
    }

    /**
     * This function for direct to phone call application.
     * The parameter for this function is activity and phone number you want to call.
     */
    public static void makeACall(final Activity activity, String phoneNumber) {
        PhoneHelper.activity = activity;
        PhoneHelper.phoneNumber = phoneNumber;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if (!phoneNumber.contentEquals("")) {
                    onConfirmationMakeACall();
                }
            } else {
                activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE);
            }
        } else {
            makeACall(activity, phoneNumber);
        }

    }

    /**
     * This function returns boolean value. True for GPS is active and false for GPS is not active
     */
    @Deprecated
    public static boolean getGPSActive(Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return provider.contains("gps") == true;
    }

    private static void onConfirmationMakeACall() {
        new MaterialDialog.Builder(activity)
                .title("Confirmation")
                .content("Do you call this number : " + phoneNumber + "?")
                .cancelable(false)
                .positiveText("Yes")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        activity.startActivity(intent);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    /**
     * This function for play default notification ringtone.
     */
    public static void onPlayDefaultNotificationRingtone(Context context) {
        try {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function for hide virtual keyboard.
     */
    public static void onHideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * This function returns storage size in byte.
     */
    public static long readStorageSize() {
        // TODO : Get a storage size
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long blockCount = stat.getBlockCount();
        return blockCount * blockSize;
    }

    /**
     * This function returns storage availabale size in byte.
     */
    public static long readAvailableStorage() {
        // TODO : Get an available storage size
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * This function for vibrate your phone and you can set length
     * a vibrate in seconds.
     */
    public static void toVibrate(Context context, int seconds) {
        // TODO : To make phone vibrate
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(seconds * 1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(seconds * 1000);
        }
    }
}
