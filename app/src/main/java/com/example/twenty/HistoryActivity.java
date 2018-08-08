package com.example.twenty;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {
    private static TextView tVAngry;
    private static TextView tVPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tVAngry = (TextView)findViewById(R.id.tvForgot);
        tVPlants = (TextView)findViewById(R.id.tvForgot);
    }

    public void onClickAngry(){
        tVAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setTitle("Angry Birds");
            }
        });
    }

}
