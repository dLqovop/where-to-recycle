package com.example.myapplication.ui.notifications.trashdetective;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseHelper {
    private DatabaseReference databaseReference;

    public FirebaseDatabaseHelper() {
        // Firebase Realtime Database의 "recycle" 노드를 참조합니다.
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("recycle");
    }

    public void fetchDataForDetectedClass(String detectedClass, final OnDataFetchListener listener) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child(detectedClass).exists()) {
                        DataSnapshot classSnapshot = dataSnapshot.child(detectedClass);

                        String disposalDay = classSnapshot.child("disposalDay").getValue(String.class);
                        String disposalMethod = classSnapshot.child("disposalMethod").getValue(String.class);
                        String type = classSnapshot.child("type").getValue(String.class);

                        // 데이터를 가져왔을 때 콜백 함수를 호출하여 데이터를 전달합니다.
                        listener.onDataFetched(disposalDay, disposalMethod, type);
                    } else {
                        // 탐지된 클래스에 해당하는 데이터가 없는 경우 콜백 함수를 호출합니다.
                        listener.onDataNotFound();
                    }
                } else {
                    // "recycle" 노드가 없는 경우 콜백 함수를 호출합니다.
                    listener.onDataNotFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터를 읽는 도중 에러가 발생한 경우 호출됩니다.
                // 에러 처리를 수행합니다.
                listener.onDataFetchError(databaseError.getMessage());
            }
        };

        databaseReference.addValueEventListener(valueEventListener);
    }

    public interface OnDataFetchListener {
        void onDataFetched(String disposalDay, String disposalMethod, String type);

        void onDataNotFound();

        void onDataFetchError(String errorMessage);
    }
}