package com.example.myapplication.ui.notifications.trashdetective;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FirebaseStorageHelper {
    private StorageReference storageReference;

    public FirebaseStorageHelper() {
        // Firebase Storage의 "images" 경로를 참조합니다.
        storageReference = FirebaseStorage.getInstance().getReference().child("images");
    }

    public void uploadImage(Uri fileUri, final OnImageUploadListener listener) {
        String uniqueFilename = UUID.randomUUID().toString(); // 고유한 파일 이름 생성
        StorageReference imageRef = storageReference.child(uniqueFilename);

        UploadTask uploadTask = imageRef.putFile(fileUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // 업로드 성공 시 다운로드 URL을 가져와서 콜백 함수를 호출합니다.
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                listener.onImageUploadSuccess(downloadUrl);
            });
        }).addOnFailureListener(exception -> {
            // 업로드 실패 시 에러 메시지를 콜백 함수를 호출합니다.
            listener.onImageUploadFailure(exception.getMessage());
        });
    }

    public interface OnImageUploadListener {
        void onImageUploadSuccess(String downloadUrl);

        void onImageUploadFailure(String errorMessage);
    }
}