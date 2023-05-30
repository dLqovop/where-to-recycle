package com.example.myapplication.ui.notifications.trashdetective;

import static com.example.myapplication.ui.notifications.trashdetective.PrePostProcessor.mClasses;

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
    TextView overlap, noOverlap;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.detected_class_result);

        //ArrayList<String> overlapResult = new ArrayList<>();
        //ArrayList<String> noOverlapResult = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<String> overlapResult = bundle.getStringArrayList("Overlap");
            ArrayList<String> noOverlapResult = bundle.getStringArrayList("NoOverlap");

            // 겹치는 결과 출력
            if (overlapResult != null && overlapResult.size() > 0) {
                StringBuilder overlapText = new StringBuilder("Overlap Detected:\n");
                for (String result : overlapResult) {
                    overlapText.append(result).append("\n");
                }
                overlap.setText(overlapText.toString());
            } else {
                overlap.setText("No Overlap Detected");
            }

            // 겹치지 않는 결과 출력
            if (noOverlapResult != null && noOverlapResult.size() > 0) {
                StringBuilder noOverlapText = new StringBuilder("No Overlap Detected:\n");
                for (String result : noOverlapResult) {
                    noOverlapText.append(result).append("\n");
                }
                noOverlap.setText(noOverlapText.toString());
            } else {
                noOverlap.setText("No Non-Overlap Detected");
            }
        }


/*
        Intent intent = getIntent();
        if (intent != null) {
            overlapResult = intent.getStringArrayListExtra("Overlap");
            noOverlapResult = intent.getStringArrayListExtra("NoOverlap");
        }

        overlap = (TextView) findViewById(R.id.overlaptextview);
        noOverlap = (TextView) findViewById(R.id.noOverlaptextview);

        Log.v("Overlap", "No overlap detected: " + noOverlapResult);

        overlap.setText(String.valueOf(overlapResult));
        noOverlap.setText(String.valueOf(noOverlapResult));
//        Result result = intent.getParcelableExtra("result");
//        if (result != null) {
//            Log.v("Overlap", "Overlap detected: " + result.toString());
//            mclassresult.setText(String.valueOf(result.toString()));
//        }
*/

        mrezero = findViewById(R.id.rezero);
        mrezero.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
