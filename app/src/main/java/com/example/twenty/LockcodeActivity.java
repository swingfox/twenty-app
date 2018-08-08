package com.example.twenty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LockcodeActivity extends AppCompatActivity {
    private static Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockcode);
        ToLoginClickButtonListener();
    }

    public void ToLoginClickButtonListener(){
        btnSave = (Button)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentToLog = new Intent(LockcodeActivity.this, LoginActivity.class);
                        startActivity(intentToLog);
                    }
                }
        );
    }

}
