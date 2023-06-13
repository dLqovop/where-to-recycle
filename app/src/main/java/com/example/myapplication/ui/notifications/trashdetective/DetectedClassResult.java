package com.example.myapplication.ui.notifications.trashdetective;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.io.File;
import java.util.ArrayList;

public class DetectedClassResult extends AppCompatActivity {
    Button mRezero;
    TextView overlapTextView, noOverlapTextView;
    ImageView mImageView;

    private FirebaseDatabaseHelper firebaseDatabaseHelper;
    private FirebaseStorageHelper firebaseStorageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detected_class_result);

        // UI 요소 초기화
        overlapTextView = findViewById(R.id.overlaptextview);
        noOverlapTextView = findViewById(R.id.noOverlaptextview);
        mImageView = findViewById(R.id.photoImageView);

        overlapTextView.setVisibility(View.GONE);
        noOverlapTextView.setVisibility(View.GONE);

        // Firebase 데이터베이스 도우미 클래스 인스턴스 생성
        firebaseDatabaseHelper = new FirebaseDatabaseHelper();
        // Firebase Storage 도우미 클래스 인스턴스 생성
        firebaseStorageHelper = new FirebaseStorageHelper();

        // 인텐트로 전달된 데이터 가져오기
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<String> overlapResult = bundle.getStringArrayList("Overlap");
            ArrayList<String> noOverlapResult = bundle.getStringArrayList("NoOverlap");
            String imagePath = bundle.getString("ImagePath");

            // 중복 결과 처리
            if (overlapResult != null && overlapResult.size() > 0) {
                overlapTextView.setVisibility(View.VISIBLE);
                StringBuilder overlapText = new StringBuilder();
                for (String result : overlapResult) {
                    overlapText.append(result).append("\n\n");
                }
                overlapTextView.setText(overlapText.toString());

                for (String detectedClass : overlapResult) {
                    // Firebase에서 중복된 클래스에 대한 데이터 가져오기
                    firebaseDatabaseHelper.fetchDataForDetectedClass(detectedClass, new FirebaseDatabaseHelper.OnDataFetchListener() {
                        @Override
                        public void onDataFetched(String disposalDay, String disposalMethod, String type) {
                            String resultText = "종류: " + type + "\n"
                                    + "배출 날짜: " + disposalDay + "\n"
                                    + "배출 방법: " + disposalMethod;

                            overlapTextView.append("\n\n" + resultText);
                        }

                        @Override
                        public void onDataNotFound() {
                            overlapTextView.append("\n\n감지된 클래스에 대한 데이터를 찾을 수 없습니다: " + detectedClass);
                        }

                        @Override
                        public void onDataFetchError(String errorMessage) {
                            overlapTextView.append("\n\n감지된 클래스의 데이터를 가져오는 중 오류가 발생했습니다: " + detectedClass);
                        }
                    });
                }
            }

            // 중복되지 않는 결과 처리
            if (noOverlapResult != null && noOverlapResult.size() > 0) {
                noOverlapTextView.setVisibility(View.VISIBLE);
                StringBuilder noOverlapText = new StringBuilder();
                for (String result : noOverlapResult) {
                    noOverlapText.append(result).append("\n\n");
                }
                noOverlapTextView.setText(noOverlapText.toString());

                for (String detectedClass : noOverlapResult) {
                    // Firebase에서 중복되지 않는 클래스에 대한 데이터 가져오기
                    firebaseDatabaseHelper.fetchDataForDetectedClass(detectedClass, new FirebaseDatabaseHelper.OnDataFetchListener() {
                        @Override
                        public void onDataFetched(String disposalDay, String disposalMethod, String type) {
                            String resultText = "종류: " + type + "\n"
                                    + "배출 날짜: " + disposalDay + "\n"
                                    + "배출 방법: " + disposalMethod;

                            noOverlapTextView.append("\n\n" + resultText);
                        }

                        @Override
                        public void onDataNotFound() {
                            noOverlapTextView.append("\n\n감지된 클래스에 대한 데이터를 찾을 수 없습니다: " + detectedClass);
                        }

                        @Override
                        public void onDataFetchError(String errorMessage) {
                            noOverlapTextView.append("\n\n감지된 클래스의 데이터를 가져오는 중 오류가 발생했습니다: " + detectedClass);
                        }
                    });
                }
            }

            // 이미지 표시
            if (imagePath != null) {
                Uri imageUri = Uri.fromFile(new File(imagePath));
                Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
                mImageView.setImageBitmap(bitmap);

                // 이미지 업로드
                firebaseStorageHelper.uploadImage(imageUri, new FirebaseStorageHelper.OnImageUploadListener() {
                    @Override
                    public void onImageUploadSuccess(String downloadUrl) {
                        // 이미지 업로드 성공 시 처리할 작업을 여기에 추가하세요.
                        // 다운로드 URL을 사용하여 필요한 작업을 수행할 수 있습니다.
                    }

                    @Override
                    public void onImageUploadFailure(String errorMessage) {
                        // 이미지 업로드 실패 시 처리할 작업을 여기에 추가하세요.
                        // 오류 메시지를 표시하거나 오류 처리를 수행할 수 있습니다.
                    }
                });
            }

            // Rezero 버튼 클릭 이벤트 처리
            mRezero = findViewById(R.id.rezero);
            mRezero.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });

            // List Button 클릭 이벤트 처리
            Button mListButton = findViewById(R.id.listButton);
            mListButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // 감지된 클래스 결과 리스트 생성
                    ArrayList<String> overlapList = new ArrayList<>();
                    overlapList.add(overlapTextView.getText().toString());
                    ArrayList<String> noOverlapList = new ArrayList<>();
                    noOverlapList.add(noOverlapTextView.getText().toString());

                    // ResultListActivity로 전환
                    Intent intent = new Intent(DetectedClassResult.this, ResultListActivity.class);
                    intent.putStringArrayListExtra("Overlap", overlapList);
                    intent.putStringArrayListExtra("NoOverlap", noOverlapList);
                    startActivity(intent);
                }
            });
        }
    }

}