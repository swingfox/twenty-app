package com.example.twenty;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * Created by David on 09/11/2016.
 */
public class TimerActivity extends AppCompatActivity {
    public static Context context;
    TextView timerTextView;
    private GoogleApiClient client;
    DonutProgress progress;
    long startTime = 0;
    int _seconds = 20;
    int _wait = 0;
    int _active = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
           // int seconds = (int) (millis / 1000);
           // int seconds = _seconds;
            int minutes = _seconds / 60;
            _seconds = _seconds % 60;
            if(_seconds >= 10)
                progress.setPrefixText("00:");
            else
                progress.setPrefixText("00:0");
            timerTextView.setText(String.format("%02d:%02d", minutes, _seconds));
            progress.setProgress(_seconds);
            if(timerTextView.getText().toString().equals("00:00")) {
                SettingsActivity.registerAlarm(SettingsActivity.getAppContext(),_wait,_active);
                //SettingsActivity.startServiceInterval(TimerActivity.context,_wait,_active);
                finish();
            }
            timerHandler.postDelayed(this, 1000);
            _seconds--;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        TimerActivity.context = this;
        progress = (DonutProgress) findViewById(R.id.progress_countDown);
        if(SettingsActivity.context != null)
            SettingsActivity.unregisterAlarm(SettingsActivity.getAppContext());
        Bundle extras = getIntent().getExtras();
        Toast.makeText(context,"active: " + _active, Toast.LENGTH_LONG);

        timerTextView = (TextView) findViewById(R.id.txtTimer);
        timerTextView.setText("00:" +_wait);

        if (extras != null) {
            _wait = extras.getInt("wait");
            _active = extras.getInt("active");
            _seconds = _wait;
            progress.setMax(_wait);
            progress.setProgress(_wait);
            progress.setTextSize(150);
        }
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        lock.disableKeyguard();

        //SettingsActivity.unregisterAlarm(SettingsActivity.getAppContext());
       // DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
      //  mDPM.setLockTaskPackages(new ComponentName("com.example.twenty", "TimerActivity"), new String[]{"com.example.twenty"});
      //  startLockTask();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

// textView is the TextView view that should display it
        Toast.makeText(getApplicationContext(),currentDateTimeString,Toast.LENGTH_LONG).show();

        Button b = (Button) findViewById(R.id.button);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
             /*   if (b.getText().equals("running...")) {

                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {*/
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                 //   b.setText("running...");
                    b.setEnabled(false);
              //  }
            }
        });

        Button unlock = (Button) findViewById(R.id.btnUnlock);
        final EditText txtLockPin = (EditText) findViewById(R.id.txtLockPin);
        unlock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    FileInputStream fin = openFileInput("lock.txt");
                    int c;
                    String temp = "";
                    while ((c = fin.read()) != -1) {
                        temp = temp + Character.toString((char) c);
                    }
                    if (temp.equals(txtLockPin.getText().toString())) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid lock code.", Toast.LENGTH_LONG).show();
                    }
                    txtLockPin.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "NOTHING!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
     //   List<ActivityManager.AppTask> a =  activityManager.getAppTasks();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Timer Page", // TODO: Define a title for the content shown.
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
                "Timer Page", // TODO: Define a title for the content shown.
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
