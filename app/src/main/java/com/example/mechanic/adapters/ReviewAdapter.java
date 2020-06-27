package com.example.mechanic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.model.Complaint;
import com.example.mechanic.model.MechRating;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

public class ReviewAdapter extends FirebaseRecyclerPagingAdapter<MechRating,ReviewAdapter.MyHolder> {
    Context c;
    private final int[] mColors = {R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};


    public ReviewAdapter(@NonNull DatabasePagingOptions<MechRating> options,Context c) {
        super(options);
        this.c = c;
    }


    @Override
    protected void onBindViewHolder(@NonNull MyHolder viewHolder, int position, @NonNull MechRating model) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 10]);
        viewHolder.cardView.setCardBackgroundColor(bgColor);
        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, null);
        return new ReviewAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView reviewText,managerName,date;
        RatingBar ratingBar;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.review_card);
            reviewText=itemView.findViewById(R.id.review_mesg_tv);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            managerName = itemView.findViewById(R.id.manager_name);
            date = itemView.findViewById(R.id.review_date);

        }

        public void bind(MechRating model) {
          managerName.setText(model.getManager().getUserName());
          reviewText.setText(model.getReviews());
          ratingBar.setRating(model.getStars());
          date.setText(model.getRevDate());

        }
    }
}
