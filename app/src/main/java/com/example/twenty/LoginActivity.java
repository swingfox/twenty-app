package com.example.twenty;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.response.FastJsonResponse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static EditText txtLock;
    private static ImageButton btnLogin;
    private static TextView tVForgot;
    private static Button btnStart;
    private GoogleApiClient client;
    public final String BROADCAST = "com.example.twenty.android.action.broadcast";
    private boolean isRegistered = false;

    public static Context getAppContext(){
        return SettingsActivity.context;
    }
    int _wait;
    int _active;
    private Bundle b;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtLock = (EditText) findViewById(R.id.editTextLock);
        btnLogin = (ImageButton) findViewById(R.id.buttonLogin);
        tVForgot = (TextView) findViewById(R.id.tvForgot);
     //   btnStart = (Button) findViewById(R.id.buttonStart);
      //  btnEnd = (Button) findViewById(R.id.buttonEnd);
    //    IntentFilter filter = new IntentFilter(BROADCAST);
  //      BroadcastReceiver mReceiver = new ScreenReceiver();
   //     registerReceiver(mReceiver, filter);
        onClickForgot();
        loginClickButtonListener();
       // createService();
      //  destroyService();
        ActivityManager activityManager = (ActivityManager) getSystemService (Context.ACTIVITY_SERVICE);



     /*       String packageName = activityManager.getRunningAppProcesses().get(0).processName;
        Toast.makeText(this,packageName+"HAHAHHAA",Toast.LENGTH_LONG).show();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = this.getPackageManager().queryIntentActivities(mainIntent, 0);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDPM.setLockTaskPackages(new ComponentName("com.example.twenty", "LoginActivity"), new String[]{"com.example.twenty"});
            startLockTask();
        }
        List<ActivityManager.AppTask> a =  activityManager.getAppTasks();
*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
         //   Toast.makeText(this, "Installed package :" + packageInfo.packageName +"\n" + "Source dir : " + packageInfo.sourceDir,Toast.LENGTH_LONG).show();
         //   Toast.makeText(this, ,Toast.LENGTH_LONG).show();
         //   Toast.makeText(this, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName),Toast.LENGTH_LONG).show();
        }
// the getLaunchIntentForPackage returns an intent that you can use with startActivity()

      //  registerAlarm();
     //   someMethod();

    }
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
/*
        public void someMethod() {
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
         //   windowManager.addView(R.layout.activity_timer,);
            Toast.makeText(this,"SOME METHOD",Toast.LENGTH_LONG).show();
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not grant..

                Log.e("SYSTEM ALERT WINDOW", "Permission not granted");
            }
        }
    }


    public void registerAlarm(){
        Intent myIntent = new Intent(this, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 60); // first time
        long frequency= 60 * 1000; // in ms
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        Bundle extra = getIntent().getExtras();
        if(extra!=null) {
            _wait = extra.getInt("wait");
            _active = extra.getInt("active");
            isRegistered = extra.getBoolean("isRegistered");
            b = extra;
        }

        Toast.makeText(this,b+" bundle",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }

    public void createService() {
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        getApplicationContext().startService(intent);
        getWindow().addFlags(0x80000000);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
           /*  IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);*/

        /*
        btnStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MyService.class);
                        getApplicationContext().startService(intent);
                        getWindow().addFlags(0x80000000);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
        );*/
    }

    public void destroyService() {
        /*btnEnd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MyService.class);
                        stopService(intent);
                        getWindow().clearFlags(0x80000000);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
        );*/
    }

    public void loginClickButtonListener() {
       final  Context c  = this;
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            FileInputStream fin = openFileInput("lock.txt");
                            int c;
                            String temp = "";
                            while ((c = fin.read()) != -1) {
                                temp = temp + Character.toString((char) c);
                            }
                            if (temp.equals(txtLock.getText().toString())) {
                                Intent intentToSet = new Intent(LoginActivity.this, SettingsActivity.class);
                               // Bundle e =
                                if(b!=null)
                                intentToSet.putExtras(b);
                                startActivity(intentToSet);
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid lock code.", Toast.LENGTH_LONG).show();
                            }
                            txtLock.setText("");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public void showToast() {
        Toast.makeText(this, "E-mail has been sent.", Toast.LENGTH_LONG).show();
    }

    public void onClickForgot() {
        tVForgot.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Reset password?");
                        builder.setMessage("New password will be sent to your e-mail address.");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
                                builder2.setMessage("Are you sure?");
                                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showToast();
                                    }
                                });
                                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog alert2 = builder2.create();
                                alert2.show();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
        );

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            // Do something here...
            event.startTracking(); // Needed to track long presses
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            // Do something here...
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.twenty/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.twenty/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
