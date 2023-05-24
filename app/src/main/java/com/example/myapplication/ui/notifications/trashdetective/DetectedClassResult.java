package com.example.myapplication.ui.notifications.trashdetective;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class DetectedClassResult extends AppCompatActivity {
    Button mrezero;
    TextView mclassresult;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.detected_class_result);

        ArrayList<String> receiveClass = new ArrayList<String>();
        Intent intent = getIntent();
        receiveClass = intent.getStringArrayListExtra("classes");

        mclassresult = (TextView) findViewById(R.id.classResult);

        Log.v("classes", String.valueOf(receiveClass));
        mclassresult.setText(String.valueOf(receiveClass));


        mrezero = findViewById(R.id.rezero);
        mrezero.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
