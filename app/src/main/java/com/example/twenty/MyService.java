package com.example.twenty;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    String CURRENT_PACKAGE_NAME = "com.example.twenty";
    String lastAppPN = "";
    boolean noDelay = false;
    public static MyService instance;
    int _wait = 0;
    int _active = 0;

    private static Timer timer = new Timer();
    public Boolean userAuth = false;
    private Context ctx;
    public String pActivity="";

    int wait;
    public static MyService myService;

    @Override
    public void onCreate(){
        super.onCreate();
        ctx = this;
        myService = this;
    }

 /*   private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(), 0, 500);
    }
*/

    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
        }
    }

    public ActivityManager.RunningAppProcessInfo getCurrent(){
        final int PROCESS_STATE_TOP = 2;
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        Field field = null;
        try {
            field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
        } catch (Exception ignored) {
        }
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo app : appList) {
            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    app.importanceReasonCode == 0 ) {
                Integer state = null;
                try {
                    state = field.getInt( app );
                } catch (Exception ignored) {
                }
                if (state != null && state == PROCESS_STATE_TOP) {
                    currentInfo = app;
                    break;
                }
            }
        }
        return currentInfo;
    }
    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String activityOnTop;
            ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
            activityOnTop=ar.topActivity.getClassName();

            if(activityOnTop.equals("com.example.twenty.LoginActivity"))
            {
                pActivity = activityOnTop.toString();
                SettingsActivity.unregisterAlarm(SettingsActivity.getAppContext());
                Toast.makeText(MyService.this, "SERVICE ALARM UNREGISTERED", Toast.LENGTH_LONG).show();
            }
            else {
                if(activityOnTop.equals(pActivity) || activityOnTop.equals("com.example.twenty.TimerActivity")||activityOnTop.equals("com.example.twenty.LoginActivity")||activityOnTop.equals("com.example.twenty.SettingsActivity")) {
                    SettingsActivity.unregisterAlarm(SettingsActivity.getAppContext());
                    Toast.makeText(MyService.this, "SERVICE ALARM UNREGISTERED", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent i = new Intent(MyService.this, TimerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    Toast.makeText(MyService.this, pActivity, Toast.LENGTH_LONG).show();
                    pActivity = activityOnTop.toString();

                }
            }
        }
    };


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId){
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
       /* scheduler.scheduleAtFixedRate
                    (new Runnable() {
                        public void run() {
                            String activityOnTop;
                            ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                            List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
                            ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
                            activityOnTop=ar.topActivity.getClassName();

                            if(activityOnTop.equals("com.example.twenty.LoginActivity"))
                            {
                                pActivity = activityOnTop.toString();
                            }
                            else
                            {
                                if(activityOnTop.equals(pActivity) || activityOnTop.equals("com.example.twenty.TimerActivity")||activityOnTop.equals("com.example.twenty.LoginActivity")) {
                                }
                                else {
                                    Intent i = new Intent(MyService.this, TimerActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    int wait = intent.getExtras().getInt("wait");
                                    int active = intent.getExtras().getInt("active");
                                    i.putExtra("wait",wait);
                                    i.putExtra("active",active);
                                    startActivity(i);
                                    pActivity = activityOnTop.toString();

                                }
                            }
                        }
                    }, 0, 1, TimeUnit.SECONDS);*/
        if(intent!=null) {
            Bundle extras = intent.getExtras();

            if (extras != null) {
                _wait = extras.getInt("wait");
                _active = extras.getInt("active");
                Toast.makeText(this, "Service Registered!!" + _wait + " sec " + _active + " mins", Toast.LENGTH_SHORT).show();
                try {
                    FileOutputStream fileWait = openFileOutput("wait.txt", MODE_PRIVATE);
                    OutputStreamWriter outputWait = new OutputStreamWriter(fileWait);
                    outputWait.write(_wait);
                    outputWait.close();
                    FileOutputStream fileActive = openFileOutput("active.txt", MODE_PRIVATE);
                    OutputStreamWriter outputActive = new OutputStreamWriter(fileActive);
                    outputActive.write(_active);
                    outputActive.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    FileInputStream fWait = openFileInput("wait.txt");
                    int c;
                    String tempWait = "";
                    while ((c = fWait.read()) != -1) {
                        tempWait = tempWait + Character.toString((char) c);
                    }
                    FileInputStream fActive = openFileInput("active.txt");
                    String tempActive = "";
                    while ((c = fActive.read()) != -1) {
                        tempActive = tempActive + Character.toString((char) c);
                    }
                    _wait = Integer.parseInt(tempWait);
                    _active = Integer.parseInt(tempActive);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                                Intent i = new Intent(MyService.this, TimerActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                int wait = intent.getExtras().getInt("wait");
                                int active = intent.getExtras().getInt("active");
                                i.putExtra("wait",wait);
                                i.putExtra("active",active);
                                startActivity(i);
                    }
                }, 1, 1, TimeUnit.MINUTES);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this,"ONDESTROY SERVICE ACTIVITY",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void checkRunningApps() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> RAP= mActivityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo processInfo: RAP ){
            Log.e("Process name ", "" + processInfo.processName);
        }
    }

    public static void stop() {
        if (instance != null) {
            instance.stopSelf();
        }
    }

    public String detectPackageName(Intent intent) {
        //boolean screenOn = intent.getBooleanExtra("screen_state", false);
        //if(!screenOn) {
        String label = null;
       ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        Iterator iterator = appProcesses.iterator();
        PackageManager pm = this.getPackageManager();
        ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (iterator.next());
        try {
            CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
            label = c.toString();
            Log.w("LABEL ", label);
        } catch (Exception e) {
            //Name not found exception
        }
    /*    ApplicationInfo applicationInfo = null;
            ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTaskInfo = activityManager.getRunningTasks(Integer.MAX_VALUE);
            for(int ii = 0; ii < runningTaskInfo.size(); ii++)
            {
                String taskName = runningTaskInfo.get(ii).baseActivity.toShortString();
                int lastIndex = taskName.indexOf("/");
                if(-1 != lastIndex)
                {
                    taskName = taskName.substring(1,lastIndex);
                }
                PackageManager packageManager = getPackageManager();
                try
                {
                    applicationInfo = packageManager.getApplicationInfo(taskName, 0);
                }
                catch (PackageManager.NameNotFoundException e) {}
                label = (String)((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");
                Log.w("LABEL ",label);
            }
*/
        return label;
    }

    public boolean isChecked(String string){
        boolean check = false;
        if (string.matches("Angry Birds"))
            check = getFromSP("cbAngryBirds");
        return check;
    }

    private boolean getFromSP(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Twenty", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

}
