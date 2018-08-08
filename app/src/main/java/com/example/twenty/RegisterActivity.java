package com.example.twenty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.security.auth.login.LoginException;

public class RegisterActivity extends AppCompatActivity {
    private static ImageButton btnSave;
    private static EditText txtEmail;
    private static EditText txtLock;
    private static EditText txtRelock;
    private final static String EMAIL="email.txt";
    private final static String LOCK="lock.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(fileExists(getApplicationContext(), "email.txt") == false){
            setContentView(R.layout.activity_register);
            txtEmail = (EditText)findViewById(R.id.editTextEmail);
            txtLock = (EditText)findViewById(R.id.editTextLock);
            txtRelock = (EditText)findViewById(R.id.editTextRelock);
            btnSave = (ImageButton)findViewById(R.id.imageButtonSave);
            ToLockClickButtonListener();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    }

    public void ToLockClickButtonListener(){
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(txtEmail.getText().toString()) ||
                                TextUtils.isEmpty(txtLock.getText().toString()) ||
                                TextUtils.isEmpty(txtRelock.getText().toString()) ||
                                txtLock.length() < 4 || txtLock.length() > 4 || txtRelock.length() > 4 ||
                                txtRelock.length() < 4 ||
                                !txtLock.getText().toString().matches(txtRelock.getText().toString()) ||
                                noAt()) {
                            txtLock.setText("");
                            txtRelock.setText("");
                            Toast.makeText(getApplicationContext(), "Username or lock codes incorrect.", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                FileOutputStream fileEmail = openFileOutput("email.txt", MODE_PRIVATE);
                                OutputStreamWriter outputEmail = new OutputStreamWriter(fileEmail);
                                outputEmail.write(txtEmail.getText().toString());
                                outputEmail.close();
                                FileOutputStream fileLock = openFileOutput("lock.txt", MODE_PRIVATE);
                                OutputStreamWriter outputLock = new OutputStreamWriter(fileLock);
                                outputLock.write(txtLock.getText().toString());
                                outputLock.close();

                                FileOutputStream wait = openFileOutput("wait.txt", MODE_PRIVATE);
                                OutputStreamWriter outputWait = new OutputStreamWriter(wait);
                                outputWait.write("");
                                outputWait.close();

                                FileOutputStream active = openFileOutput("active.txt", MODE_PRIVATE);
                                OutputStreamWriter outputActive = new OutputStreamWriter(active);
                                outputActive.write("");
                                outputActive.close();

                                FileOutputStream isRegistered = openFileOutput("isRegistered.txt", MODE_PRIVATE);
                                OutputStreamWriter outputIsRegistered = new OutputStreamWriter(isRegistered);
                                outputIsRegistered.write("");
                                outputIsRegistered.close();

                                //display file saved message
                                Toast.makeText(getBaseContext(), "Successfully signed-in!",
                                        Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            /*Bundle bundle = new Bundle();
                            bundle.putString("email", txtEmail.getText().toString());
                            bundle.putString("lock", txtLock.getText().toString());
                            bundle.putString("relock", txtRelock.getText().toString());
                            intent.putExtras(bundle);*/
                            startActivity(intent);
                        }
                    }
                }
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean noAt(){
        boolean has=true;
        for (int i = 0, n = txtEmail.getText().toString().length(); i < n; i++) {
            if(txtEmail.getText().toString().charAt(i) == '@'){
                has=false;
                break;
            }
        }
        return has;
    }

    public boolean fileExists(Context context, String filename){
        File file = context.getFileStreamPath("email.txt");
        if(file==null||!file.exists()){
            return false;
        }
        return true;
    }
}
