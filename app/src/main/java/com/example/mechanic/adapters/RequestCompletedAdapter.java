package com.example.mechanic.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.model.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

import java.util.List;

public class RequestCompletedAdapter extends FirebaseRecyclerPagingAdapter<Request, RequestCompletedAdapter.MyHolder> {

    Context c;
    private final int[] mColors = {R.color.list_color_1,R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};

    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */

    public RequestCompletedAdapter(DatabasePagingOptions<Request> options, Context c) {
        super(options);
        this.c = c;
    }

    @NonNull
    @Override
    public RequestCompletedAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_completed_item, null);
        return new RequestCompletedAdapter.MyHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestCompletedAdapter.MyHolder holder, int position, Request model) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 12]);
        holder.cardview.setCardBackgroundColor(bgColor);
        holder.bind(model);

    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }



    public class MyHolder extends RecyclerView.ViewHolder{

        TextView request_id , responsiblemanName ,  description , complain_id, approvedDate ;
        CardView cardview;
        LinearLayout ll_hide;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            request_id = itemView.findViewById(R.id.s_RecyclerView_requestID__com_req);
            responsiblemanName = itemView.findViewById(R.id.s_RecyclerView_ResponsibleMan_com_req);
            description = itemView.findViewById(R.id.s_RecyclerView_Description_com_req);
            complain_id = itemView.findViewById(R.id.s_RecyclerView_ComplainID_com_req);
            approvedDate = itemView.findViewById(R.id.RecyclerView_Date);
            cardview = itemView.findViewById(R.id.s_cardview_com_req);
            ll_hide=  itemView.findViewById(R.id.s_ll_hide_com_req);
            ll_hide.setVisibility(View.GONE);

            cardview.setOnClickListener(new View.OnClickListener() {                //Expandable card feature
                @Override
                public void onClick(View v) {

                    if(ll_hide.getVisibility()==View.GONE)
                        ll_hide.setVisibility(View.VISIBLE);
                    else
                        ll_hide.setVisibility(View.GONE);
                }
            });

        }

        public void bind(Request model)
        {
            responsiblemanName.setText(model.getComplaint().getManager().getUserName());
            description.setText(model.getDescription());
            complain_id.setText(String.valueOf((int) model.getComplaint().getComplaintId()));
            request_id.setText(String.valueOf((int) model.getRequestId()));
            approvedDate.setText(model.getApprovedDate());

//        boolean isExpanded = x.get(position).isExpanded();
        }
    }


}
