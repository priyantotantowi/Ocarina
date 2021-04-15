package me.pete.ocarinalibrary.helper;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;
import me.pete.ocarinalibrary.R;

public final class ColorHelper {
    public static int getAccentColorFromThemeIfAvailable(Context context) {
        TypedValue typedValue = new TypedValue();
        // First, try the android:colorAccent
        if (Build.VERSION.SDK_INT >= 21) {
            context.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
            return typedValue.data;
        }
        // Next, try colorAccent from support lib
        int colorAccentResId = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
        if (colorAccentResId != 0 && context.getTheme().resolveAttribute(colorAccentResId, typedValue, true)) {
            return typedValue.data;
        }
        // Return the value in mdtp_accent_color
        return ContextCompat.getColor(context, R.color.colorAccent);
    }
}
