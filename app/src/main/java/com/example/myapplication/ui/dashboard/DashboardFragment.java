package com.example.myapplication.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    //private FragmentDashboardBinding binding;
    private WebView mWebView;

    /*public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }*/
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            DashboardViewModel dashboardViewModel =
                    new ViewModelProvider(this).get(DashboardViewModel.class);

            View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
            //View root = binding.getRoot();

            mWebView = root.findViewById(R.id.mWebView);

            // WebView 설정
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            // WebViewClient 설정
            mWebView.setWebViewClient(new WebViewClient());

            // 웹 페이지 로드
            mWebView.loadUrl("https://cupscout.github.io/");

            return root;
        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}