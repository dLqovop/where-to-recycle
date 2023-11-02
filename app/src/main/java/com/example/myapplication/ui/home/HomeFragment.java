package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {
    TextView textView4;
    private FragmentHomeBinding binding;

    Button button_week, button_paper, button_can, button_glass, button_plastic, button_vinyl, button_styro;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        button_week=root.findViewById(R.id.button_week);

        button_paper=root.findViewById(R.id.button_paper);
        button_can=root.findViewById(R.id.button_can);
        button_glass=root.findViewById(R.id.button_glass);
        button_plastic=root.findViewById(R.id.button_plastic);
        button_vinyl=root.findViewById(R.id.button_vinyl);
        button_styro=root.findViewById(R.id.button_styro);

        //요일별 분리배출 화면
        button_week.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getContext(),homeweektrash.class);
                startActivity(intent);
            }
        });

        //종류별 분리배출 방법 화면

        //종이류
        button_paper.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getContext(), trash_paper.class);
                startActivity(intent);
            }
        });

        //캔류
        button_can.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getContext(), trash_can.class);
                startActivity(intent);
            }
        });

        //유리류
        button_glass.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getContext(), trash_glass.class);
                startActivity(intent);
            }
        });

        //플라스틱류
        button_plastic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getContext(), trash_plastic.class);
                startActivity(intent);
            }
        });

        //비닐류
        button_vinyl.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getContext(), trash_vinyl.class);
                startActivity(intent);
            }
        });

        //스티로폼
        button_styro.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getContext(), trash_styro.class);
                startActivity(intent);
            }
        });


        textView4=root.findViewById(R.id.textView4);

        final TextView textView = binding.textView4;


        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}