package com.example.myapplication.ui.notifications.trashdetective;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ResultViewHolder> {
    private List<String> resultList;

    public ResultListAdapter(List<String> resultList) {
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        String result = resultList.get(position);
        holder.bind(result);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        private TextView resultTextView;


        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            resultTextView = itemView.findViewById(R.id.resultTextView);

        }

        public void bind(String result) {
            resultTextView.setText(result);
        }
    }
}
