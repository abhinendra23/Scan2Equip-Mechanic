package com.example.mechanic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.model.Complaint;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

public class ReviewAdapter extends FirebaseRecyclerPagingAdapter<Complaint,ReviewAdapter.MyHolder> {
    Context c;


    public ReviewAdapter(@NonNull DatabasePagingOptions<Complaint> options,Context c) {
        super(options);
        this.c = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder viewHolder, int position, @NonNull Complaint model) {
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
        TextView review_text;
        ImageView img1,img2,img3,img4,img5;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.review_card);
            review_text=itemView.findViewById(R.id.review_mesg_tv);
            img1= itemView.findViewById(R.id.star1);
            img2= itemView.findViewById(R.id.star2);
            img3= itemView.findViewById(R.id.star3);
            img4= itemView.findViewById(R.id.star4);
            img5= itemView.findViewById(R.id.star5);

        }

        public void bind(Complaint model) {

        }
    }
}
