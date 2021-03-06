package com.cmsc436.quickbite.slidingtab.ListElements;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmsc436.quickbite.Bite;
import com.cmsc436.quickbite.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lianaalvarez on 5/16/16.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BiteViewHolder>{

    public static class BiteViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView name;
        TextView time;
        TextView review;

        BiteViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.card1);
            name = (TextView)itemView.findViewById(R.id.card1name);
            time = (TextView)itemView.findViewById(R.id.card1time);
            review = (TextView)itemView.findViewById(R.id.card1review);
        }
    }
    ArrayList<Bite> bites;

    RVAdapter(ArrayList<Bite> bites){
        this.bites = bites;
    }

    @Override
    public int getItemCount() {
        return bites.size();
    }

    @Override
    public BiteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_rv, viewGroup, false);
        BiteViewHolder b = new BiteViewHolder(v);
        return b;
    }

    @Override
    public void onBindViewHolder(BiteViewHolder biteViewHolder, int i) {
        biteViewHolder.name.setText(bites.get(i).getAuthor());

        SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.US);
        Date date = new Date(bites.get(i).getTimestamp());
        String text = format.format(date);
        biteViewHolder.time.setText(text);
        biteViewHolder.review.setText(bites.get(i).getContent());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}