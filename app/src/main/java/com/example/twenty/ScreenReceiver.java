package com.example.twenty;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.twenty.database.ApkInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScreenReceiver extends BroadcastReceiver {
    private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent) {
    /*    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getService(context, 0, new Intent(context, MyService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, 1000, pi);*/
        screenOff = false;


      /*  j.setClassName("com.example.twenty","com.example.twenty.TimerActivity");
        j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(j);*/


        Toast.makeText(context,"START RECEIVE " + intent.getExtras().getInt("wait") + " sec",Toast.LENGTH_SHORT).show();

      /*  if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            screenOff = true;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;
        }*//*
        Toast.makeText(context,"START RECEIVE",Toast.LENGTH_SHORT).show();
        Intent i = new Intent();
        i.setClassName("com.example.twenty", "com.example.twenty.MyService");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);*/
        /*Toast.makeText(context, "SCREEN RECEIVER", Toast.LENGTH_LONG).show();

        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        // get the info from the currently running task
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);

        Log.d("topActivity", "CURRENT Activity ::"
                + taskInfo.get(0).topActivity.getClassName());

        ComponentName componentInfo = taskInfo.get(0).topActivity;
               Toast.makeText(context, componentInfo.getPackageName(), Toast.LENGTH_LONG).show();*/

        int wait = intent.getExtras().getInt("wait");
        int active = intent.getExtras().getInt("active");
        Intent i = new Intent(context, TimerActivity.class);
        //  Intent j = new Intent();
        //  i.putExtra("screen_state", screenOff);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.e("HAHAHHAHAHAHA",wait+" SCREEN RECEIVER");
       // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("wait",wait);
        i.putExtra("active",active);
        context.startActivity(i);
     //   context.starS
    }
}
