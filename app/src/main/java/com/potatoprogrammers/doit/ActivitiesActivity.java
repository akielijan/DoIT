package com.potatoprogrammers.doit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView test = findViewById(R.id.textActivites);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.setText("Clicked!");
            }
        });
    }
}
