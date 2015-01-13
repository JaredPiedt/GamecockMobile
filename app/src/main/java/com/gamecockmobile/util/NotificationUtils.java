package com.gamecockmobile.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.gamecockmobile.R;
import com.gamecockmobile.events.EventDetailsActivity;

/**
 * Created by piedt on 10/7/14.
 */
public class NotificationUtils {

    public static void createNotifications(Context context, String title, String event, long time) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(event);

        // Create an explicit intent for EventDetailsActivity
        Intent resultIntent = new Intent(context, EventDetailsActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(EventDetailsActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0, PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // an id allows you to update the notification later on
        mNotificationManager.notify(1, mBuilder.build());

    }
}
