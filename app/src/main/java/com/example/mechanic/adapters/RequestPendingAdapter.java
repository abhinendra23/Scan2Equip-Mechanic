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

import java.util.List;

public class RequestPendingAdapter extends  RecyclerView.Adapter<RequestPendingAdapter.MyHolder1>{

    Context c;
    List<Request> x ;      //Define your list here    - Aditya

    public RequestPendingAdapter(Context c, List<Request> x)                                               //Enter the type of data in the space for model
    {
        this.c = c;
        this.x = x;
    }

    @NonNull
    @Override
    public RequestPendingAdapter.MyHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_request_item,null);
        return new RequestPendingAdapter.MyHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestPendingAdapter.MyHolder1 myholder1, int position) {


        myholder1.responsiblemanName.setText(x.get(position).getResponsiblemanName());
        myholder1.description.setText(x.get(position).getDescription());
        myholder1.complain_id.setText(x.get(position).getComplaintId());
        myholder1.request_id.setText(x.get(position).getRequestid());

        Log.i("asdf","fgh");

        boolean isExpanded = x.get(position).isExpanded();
        myholder1.ll_hide.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return x.size();                                                                                   // Return item count from firebase
    }


    class MyHolder1 extends RecyclerView.ViewHolder{


        TextView request_id , responsiblemanName ,  description , complain_id ;
        CardView cardview;
        LinearLayout ll_hide;

        public MyHolder1(@NonNull View itemView) {
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
    }

}
