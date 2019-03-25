package com.example.soulocean1.workdome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        //int mode=0;
       // String mode=intent.getStringExtra("modec");


        int mode =intent.getIntExtra("modec",2);


        Toast.makeText(context, mode+" 您有一个备忘卡到达时间啦, 可以做点事情了~", Toast.LENGTH_LONG).show();

        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.sendNotification("您有一个备忘卡到达时间啦", "内容已隐藏，请登陆查看",mode);


    }





}
