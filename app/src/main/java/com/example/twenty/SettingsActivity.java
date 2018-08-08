package com.example.twenty;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SettingsActivity extends AppCompatActivity {
    public static Context context;
    public static Spinner spinnerActive;
    public static Spinner spinnerWait;
    public static ImageButton btnApps;
    public static ImageButton btnHistory;
    public static ImageButton btnResetLock;
    public static ImageButton btnChangeEmail;
    public static ImageButton imageButtonSave;
    public static ImageButton imageButtonExit;
    private boolean isRegistered = false;

    public static Context getAppContext(){
        return SettingsActivity.context;
    }
    int _wait;
    int _active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnHistory = (ImageButton)findViewById(R.id.buttonHistory);
        imageButtonExit = (ImageButton) findViewById(R.id.imageButtonExit);
        imageButtonSave = (ImageButton) findViewById(R.id.imageButtonSave);
        SettingsActivity.context = getApplicationContext();

       /* Toast.makeText(context, checkExistingAlarm() +"CHECK FOR ALARMS",
                Toast.LENGTH_LONG).show();*/
        imageButtonSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(!isRegistered){
                    String[] wait = ((String) spinnerWait.getSelectedItem()).split(" ");
                    String[] active = ((String) spinnerActive.getSelectedItem()).split(" ");;

                ///    registerAlarm(context,Integer.parseInt(wait[0]),1);
                    isRegistered = true;
                    _wait = Integer.parseInt(wait[0]);
                    _active = Integer.parseInt(active[0]);
                    registerAlarm(SettingsActivity.context,_wait,_active);
                    imageButtonSave.setImageResource(R.drawable.removealarm);
                }
                else{
               //     unregisterAlarm(context);
                    isRegistered = false;
                    unregisterAlarm(SettingsActivity.context);
                    Toast.makeText(context, "Service Unregistered!!!",
                            Toast.LENGTH_SHORT).show();
                    imageButtonSave.setImageResource(R.drawable.setalarm);
                }
            }
        });

        imageButtonExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();

            }
        });
        btnHistory.setOnClickListener(new OnClickListener() {
            @Override

            //History Dialog
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.history_dialog);
                //dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                Toast.makeText(context,"ALARM SET: " + checkAlarm(),Toast.LENGTH_LONG).show();
                Button exitButton = (Button) dialog.findViewById(R.id.imageButtonExit);
                exitButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

//                dialog.show();
                /*
                Window window = dialog.getWindow();
                float h = (float) (getResources().getDisplayMetrics().heightPixels * .9);
                float w = (float) (getResources().getDisplayMetrics().widthPixels);
                int height = (int)h;
                int width = (int)w;
                window.setLayout(width,height);*/
            }
        });

        btnApps = (ImageButton)findViewById(R.id.buttonApps);
        btnApps.setOnClickListener(new OnClickListener() {
            @Override

            //App List Dialog
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.apps_dialog);
                //dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                final CheckBox cbAngryBirds = (CheckBox)dialog.findViewById(R.id.checkBoxAngryBirds);
                cbAngryBirds.setChecked(getFromSP("cbAngryBirds"));

                Button saveButton = (Button) dialog.findViewById(R.id.buttonSave);
                saveButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cbAngryBirds.isChecked()){
                            saveInSp("cbAngryBirds", true);
                        }else
                            saveInSp("cbAngryBirds", false);
                        dialog.dismiss();
                    }
                });

                Button exitButton = (Button) dialog.findViewById(R.id.imageButtonExit);
                exitButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                float h = (float) (getResources().getDisplayMetrics().heightPixels * .9);
                float w = (float) (getResources().getDisplayMetrics().widthPixels);
                int height = (int)h;
                int width = (int)w;
                window.setLayout(width,height);
            }
        });

        btnResetLock = (ImageButton)findViewById(R.id.buttonResetLock);
        btnResetLock.setOnClickListener(new OnClickListener() {
            @Override

            //Reset Lock Dialog
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.resetlock_dialog);
                //dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                Button saveButton = (Button) dialog.findViewById(R.id.buttonSave);
                saveButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText txtOldLock = (EditText) dialog.findViewById(R.id.editTextOldLock);
                        EditText txtNewLock = (EditText) dialog.findViewById(R.id.editTextNewLock);
                        EditText txtReNew = (EditText) dialog.findViewById(R.id.editTextRetypeNew);
                        if (TextUtils.isEmpty(txtOldLock.getText().toString()) ||
                                TextUtils.isEmpty(txtNewLock.getText().toString()) ||
                                TextUtils.isEmpty(txtReNew.getText().toString()) ||
                                txtOldLock.length() < 4 ||
                                txtNewLock.length() < 4 ||
                                txtReNew.length() < 4 ||
                                !locksAreEqual(txtOldLock.getText().toString()) ||
                                !txtNewLock.getText().toString().matches(txtReNew.getText().toString())) {
                            txtOldLock.setText("");
                            txtNewLock.setText("");
                            txtReNew.setText("");

                            Toast.makeText(getApplicationContext(), "One or more lock codes are incorrect.", Toast.LENGTH_LONG).show();
                        } else {
                            changeLock(txtNewLock.getText().toString());
                            txtOldLock.setText("");
                            txtNewLock.setText("");
                            txtReNew.setText("");
                            dialog.dismiss();
                        }
                    }
                });

                Button exitButton = (Button) dialog.findViewById(R.id.imageButtonExit);
                exitButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText txtOldLock = (EditText) dialog.findViewById(R.id.editTextOldLock);
                        EditText txtNewLock = (EditText) dialog.findViewById(R.id.editTextNewLock);
                        EditText txtReNew = (EditText) dialog.findViewById(R.id.editTextRetypeNew);
                        txtOldLock.setText("");
                        txtNewLock.setText("");
                        txtReNew.setText("");
                        dialog.dismiss();
                    }
                });

                    dialog.show();
                    Window window = dialog.getWindow();
                    float h = (float) (getResources().getDisplayMetrics().heightPixels * .9);
                    float w = (float) (getResources().getDisplayMetrics().widthPixels);
                    int height = (int) h;
                    int width = (int) w;
                    window.setLayout(width,height);
                }
            }

            );

            btnChangeEmail=(ImageButton)findViewById(R.id.buttonChangeEmail);
            btnChangeEmail.setOnClickListener(new OnClickListener() {
                @Override

                //Change Email Dialog
                public void onClick (View arg0){
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.email_dialog);
                    //dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    final EditText txtNewEmail = (EditText)dialog.findViewById(R.id.editTextNewEmail);
                    txtNewEmail.setText(getEmail());

                    Button dialogButton = (Button) dialog.findViewById(R.id.buttonSave);
                    dialogButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //EditText txtNewEmail = (EditText)dialog.findViewById(R.id.editTextNewEmail);
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Are you sure?");
                            alert.setMessage("Use " + txtNewEmail.getText().toString() + " as your new address?");
                            alert.setCancelable(false);
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(TextUtils.isEmpty(txtNewEmail.getText().toString()) || noAt(txtNewEmail.getText().toString()))
                                        Toast.makeText(context, "Invalid email address.", Toast.LENGTH_LONG).show();
                                    else{
                                        try {
                                            FileOutputStream fileEmail = openFileOutput("email.txt", MODE_PRIVATE);
                                            OutputStreamWriter outputEmail = new OutputStreamWriter(fileEmail);
                                            outputEmail.write(txtNewEmail.getText().toString());
                                            outputEmail.close();

                                            //display file saved message
                                            Toast.makeText(context, "Successfully updated!",
                                                    Toast.LENGTH_SHORT).show();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = alert.create();
                            alertDialog.show();
                        }
                    });

                    Button exitButton = (Button) dialog.findViewById(R.id.imageButtonExit);
                    exitButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "Successfully exited!",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    Window window = dialog.getWindow();
                    float h = (float) (getResources().getDisplayMetrics().heightPixels * .9);
                    float w = (float) (getResources().getDisplayMetrics().widthPixels);
                    int height = (int) h;
                    int width = (int) w;
                    window.setLayout(width, height);
                }
            }

            );

            spinnerActive=(Spinner)

            findViewById(R.id.spinnerActive);

            spinnerWait=(Spinner)

            findViewById(R.id.spinnerWait);

            ArrayAdapter adapterActive = ArrayAdapter.createFromResource(this, R.array.active, android.R.layout.simple_dropdown_item_1line);
            ArrayAdapter adapterWait = ArrayAdapter.createFromResource(this, R.array.wait, android.R.layout.simple_dropdown_item_1line);
            spinnerActive.setAdapter(adapterActive);
            spinnerWait.setAdapter(adapterWait);
        }

    public boolean checkExistingAlarm(){
        boolean ok = false;
        Intent intent = new Intent(this, ScreenReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, 0);
        if(pi!=null)
            ok =true;
        return ok;
    }

    public boolean checkAlarm(){
        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(context,ScreenReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        return isRegistered;
    }

    public boolean locksAreEqual(String str){
        boolean result = false;
        try {
            FileInputStream fin = openFileInput("lock.txt");
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            if (temp.equals(str))
                result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    @Override
    protected void onStart() {
        super.onStart();
       /* FileInputStream fin = null;
        FileInputStream fia = null;
        FileInputStream fir = null;

        try {
            fin = openFileInput("wait.txt");
            fia = openFileInput("active.txt");
            fir = openFileInput("isRegistered.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int c;
        String temp = "";
        try {
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            try {
                _wait = Integer.parseInt(temp);
            }catch(NumberFormatException e){
                _wait = 20;
            }
            temp = "";
            while ((c = fia.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            try {
                _active = Integer.parseInt(temp);
            }catch(NumberFormatException e){
                _active = 20;
            }
            temp = "";
            while ((c = fir.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            try {
                isRegistered = Boolean.parseBoolean(temp);
            }catch(Exception e){
                isRegistered = false;
            }
            temp = "";
            if(!isRegistered) {
                registerAlarm(context,_wait,_active);
                imageButtonSave.setImageResource(R.drawable.removealarm);
            }
            else
                imageButtonSave.setImageResource(R.drawable.setalarm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle e = getIntent().getExtras();
        if(e!=null){
            isRegistered = e.getBoolean("isRegistered");
            _wait = e.getInt("wait");
            _active = e.getInt("active");
            if(!isRegistered) {
                registerAlarm(context,_wait,_active);
                imageButtonSave.setImageResource(R.drawable.removealarm);
            }
            else
                imageButtonSave.setImageResource(R.drawable.setalarm);
        }
*/
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Intent i = new Intent(context, LoginActivity.class);
        Bundle extra = new Bundle();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("wait",_wait);
        i.putExtra("active",_active);
        i.putExtra("isRegistered",isRegistered);
      /*  FileOutputStream file = null;
        try {
            file = openFileOutput("wait.txt", MODE_PRIVATE);
            OutputStreamWriter output = new OutputStreamWriter(file);
            output.write(_wait);
            output.close();
            file = openFileOutput("active.txt", MODE_PRIVATE);
            output = new OutputStreamWriter(file);
            output.write(_active);
            output.close();
            file = openFileOutput("isRegistered.txt", MODE_PRIVATE);
            output = new OutputStreamWriter(file);
            output.write(isRegistered+"");
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        startActivity(i);
    }

    public static void registerAlarm(Context context,int wait,int active) {
        Intent i = new Intent(context, ScreenReceiver.class);
        int REQUEST_CODE = 1;
        i.putExtra("wait",wait);
        i.putExtra("active",active);
        PendingIntent sender = PendingIntent.getBroadcast(context,REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Toast.makeText(context,"ALARM SET for " + active + " mins",Toast.LENGTH_LONG).show();
        // We want the alarm to go off 3 seconds from now.
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += active * 60000;//start 3 seconds after first register.

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context
                .getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                active * 60000, sender);//10min interval

    }

    public static void unregisterAlarm(Context context) {
        Intent i = new Intent(context, ScreenReceiver.class);
        int REQUEST_CODE = 1;
        PendingIntent sender = PendingIntent.getBroadcast(context,REQUEST_CODE, i, 0);
        Toast.makeText(context,"ALARM REMOVED",Toast.LENGTH_LONG).show();
        AlarmManager aManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        aManager.cancel(sender);
    }

    public void changeLock(String str){
        try {
            FileOutputStream fileLock = openFileOutput("lock.txt", MODE_PRIVATE);
            OutputStreamWriter outputLock = new OutputStreamWriter(fileLock);
            outputLock.write(str);
            outputLock.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "Successfully updated!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        String temp = "";
        try {
            FileInputStream fin = openFileInput("email.txt");
            int c;
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public boolean noAt(String str){
        boolean has=true;
        for (int i = 0, n = str.length(); i < n; i++) {
            if(str.charAt(i) == '@'){
                has=false;
                break;
            }
        }
        return has;
    }

    private boolean getFromSP(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Twenty", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    private void saveInSp(String key,boolean value){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Twenty", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    }
