package me.pete.ocarinalibrary.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * Created by Priyanto Tantowi.
 *
 * NotificationHelper is helper to all about notification.
 */
public final class NotificationHelper {
    /**
     * This function create default notification bar with default ringtone.
     *
     * @param context   Your context for show notification
     * @param title     Title notification
     * @param content   Your message on notification
     * @param logo      To place logo your notification
     * @param myClass   The class for calling after click your notification.
     */
    public static void create(Context context, String title, String content, int logo, Class myClass){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int NOTIFICATION_ID = 234;

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                String CHANNEL_ID = "my_channel_01";
                CharSequence name = "my_channel";
                String Description = "This is my channel";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(true);
                    notificationManager.createNotificationChannel(mChannel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(logo)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);


                Intent resultIntent = new Intent(context, myClass);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(myClass);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(resultPendingIntent);
                Notification notification = builder.build();
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } else {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(logo)
                                .setContentTitle(title)
                                .setContentText(content);
                Intent resultIntent;
                resultIntent = new Intent(context, myClass);
                resultIntent.putExtra("notification", content);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(myClass);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancelAll();
                mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                mBuilder.setAutoCancel(true);
                Notification notification = mBuilder.build();
                mNotificationManager.notify(0, notification);
            }
        } catch (Exception e) {

        }
    }
}
