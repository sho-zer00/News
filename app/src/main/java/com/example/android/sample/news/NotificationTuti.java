package com.example.android.sample.news;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by sho on 2016/12/06.
 */

public class NotificationTuti {

    private static final int REQUEST_MAIN_ACTIVITY = 1;

    private NotificationTuti(){}

    public static void notifyUpdate(Context context,int newLinks){

        //通知タップでアクティビティを開く
        Intent intent = new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent viewIntent =
                PendingIntent.getActivity(context,REQUEST_MAIN_ACTIVITY,
                        intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //通知オブジェクトを作る
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_message,newLinks))
                .setContentIntent(viewIntent)
                .setAutoCancel(true)//タップすると通知が消え
                .build();

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //通知する
        notificationManager.notify(REQUEST_MAIN_ACTIVITY,notification);
    }
}
