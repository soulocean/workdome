package com.example.soulocean1.workdome;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationUtils extends ContextWrapper {
    private static final String TAG = "VVV";
    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";



    public NotificationUtils(Context context){
        super(context);
    }

    public void createNotificationChannel(int modec){
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);

        if (modec==0)
        {
            channel.enableVibration(true);
        }

        Log.d(TAG,""+modec);

        getManager().createNotificationChannel(channel);

    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    public Notification.Builder getChannelNotification(String title, String content){





        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                //.setDefaults(DEFAULT_SOUND)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);

    }
    public NotificationCompat.Builder getNotification_25(String title, String content){




        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                //.setVibrate(new long[]{1000, 0, 0})
                .setAutoCancel(true);
    }
    public void sendNotification(String title, String content,int mode){
        if (Build.VERSION.SDK_INT>=26){
            createNotificationChannel(mode);

            Notification notification = getChannelNotification(title, content).build();
            getManager().notify(1,notification);

        }else{

            Notification notification = getNotification_25(title, content).build();
            getManager().notify(1,notification);
        }
    }


}
