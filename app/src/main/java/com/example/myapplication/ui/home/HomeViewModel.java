package com.example.myapplication.ui.home;

import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeViewModel extends ViewModel {

   private final MutableLiveData<String> mText;
    public HomeViewModel() {
        Date currentTime= Calendar.getInstance().getTime();
        String date_text=new SimpleDateFormat("EE요일", Locale.getDefault()).format(currentTime);
        mText = new MutableLiveData<>();
        mText.setValue(date_text);
    }

    public LiveData<String> getText() {
        return mText;
    }
}