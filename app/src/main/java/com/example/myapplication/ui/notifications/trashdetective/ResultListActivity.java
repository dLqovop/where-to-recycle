package com.example.myapplication.ui.notifications.trashdetective;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ResultListActivity extends AppCompatActivity {
    private ArrayList<String> resultList;
    private RecyclerView recyclerView;
    private ResultListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        resultList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ResultListAdapter(resultList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            //ArrayList<String> overlapResult = intent.getStringArrayListExtra("Overlap"); //오버랩 안함
            ArrayList<String> noOverlapResult = intent.getStringArrayListExtra("NoOverlap");//감지된 품목 단일 리스트

//            if (overlapResult != null) {
//                resultList.addAll(overlapResult);
//            } //오버랩 안함

            if (noOverlapResult != null) {
                resultList.addAll(noOverlapResult);
            } //감지된 품목 단일 리스트

            adapter.notifyDataSetChanged();
        }
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 홈으로 돌아가는 동작을 수행합니다.
                Intent intent = new Intent(ResultListActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티를 종료합니다.
            }
        });


    }

}