package com.example.englishwordslearning.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishwordslearning.R;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private String[] englishWord;
    private String[] russianWord;
    private String[] statistic;

    public RecyclerAdapter(String[] englishWord, String[] russianWord, String[] statistic) {
        this.englishWord = englishWord;
        this.russianWord = russianWord;
        this.statistic = statistic;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_for_recycler,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView englishTextView = (TextView) cardView.findViewById(R.id.english_word_recycler);
        TextView russianTextView = (TextView) cardView.findViewById(R.id.russian_word_recycler);
        TextView stat = cardView.findViewById(R.id.statistic_word_recycler);
        englishTextView.setText(englishWord[position]);
        russianTextView.setText(russianWord[position]);
        stat.setText(statistic[position]);
    }

    @Override
    public int getItemCount() {
      return englishWord.length;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }
    }
}
