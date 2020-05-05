package com.example.inclass07;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TriviaAdapter extends RecyclerView.Adapter<TriviaAdapter.ViewHolder> {
    String[] data;
    private static final String TAG = "demo";
    public static InteractWithTriviaActivity interact;
    Context ctx;
    int row_index = -1;
//    TriviaActivity triviaActivity = new TriviaActivity();

    public TriviaAdapter(String[] data, Context triviaActivity) {
        this.data = data;
        this.ctx = triviaActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder called");
        holder.textViewOption.setText(data[position]);
        interact = (InteractWithTriviaActivity) ctx;

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interact.getSelectedOption(position, data[position]);
                row_index=position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.cardView.setBackgroundColor(Color.parseColor("#567845"));
            holder.textViewOption.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.textViewOption.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount called ");
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewOption;
        CardView cardView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
//            Log.d(TAG, "ViewHolder constructor called");
            textViewOption = itemView.findViewById(R.id.textViewOption);
            cardView = itemView.findViewById(R.id.cardView);
        }

    }
    public interface InteractWithTriviaActivity{
        void getSelectedOption(int position, String s);
    }
}
