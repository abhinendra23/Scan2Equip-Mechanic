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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.model.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

import java.util.List;


public class RequestPendingAdapter extends FirebaseRecyclerPagingAdapter<Request, RequestPendingAdapter.MyHolder> {

    Context c;
    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    public RequestPendingAdapter(DatabasePagingOptions<Request> options, Context c) {
        super(options);
        this.c = c;

    }

    public RequestPendingAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_request_item,null);
        return new RequestPendingAdapter.MyHolder(view);
    }

    protected void onBindViewHolder(RequestPendingAdapter.MyHolder viewHolder, int position, Request model) {
        viewHolder.bind(model);
    }
    protected void onLoadingStateChanged(LoadingState state){

    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView request_id , responsiblemanName ,  description , complain_id ;
        CardView cardview;
        LinearLayout ll_hide;

        public MyHolder(final View itemView)
        {
            super(itemView);

            request_id = itemView.findViewById(R.id.s_RecyclerView_requestID__pen_req);
            responsiblemanName = itemView.findViewById(R.id.s_RecyclerView_ResponsibleMan_pen_req);
            description = itemView.findViewById(R.id.s_RecyclerView_Description_pen_req);
            complain_id = itemView.findViewById(R.id.s_RecyclerView_ComplainID_pen_req);
            cardview = itemView.findViewById(R.id.s_cardview_pen_req);
            ll_hide=  itemView.findViewById(R.id.s_ll_hide_pen_req);
            ll_hide.setVisibility(View.INVISIBLE);

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

        public void bind(Request model) {
            responsiblemanName.setText(model.getComplaint().getManager().getUserName());
            description.setText(model.getDescription());
            complain_id.setText(String.valueOf(model.getComplaint().getComplaintId()));
            request_id.setText(String.valueOf(model.getRequestId()));

            Log.i("asdf","fgh");

    //        boolean isExpanded = x.get(position).isExpanded();
            boolean isExpanded = true;
            ll_hide.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }


    }
}
