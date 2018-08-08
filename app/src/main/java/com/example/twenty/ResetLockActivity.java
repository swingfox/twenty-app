package com.example.twenty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ResetLockActivity extends AppCompatActivity {
    public static Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetlock);

        toSettingsClickButtonListener();
    }

    public void toSettingsClickButtonListener(){
        btnSave = (Button)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentToSettings = new Intent(ResetLockActivity.this, SettingsActivity.class);
                        startActivity(intentToSettings);
                    }
                }
        );
    }
}
