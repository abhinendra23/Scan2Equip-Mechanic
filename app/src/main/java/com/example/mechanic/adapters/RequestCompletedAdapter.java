package com.example.mechanic.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.model.Request;

import java.util.List;

public class RequestCompletedAdapter extends RecyclerView.Adapter<RequestCompletedAdapter.MyHolder> {

    Context c;
    List<Request> completedRequest;

    public RequestCompletedAdapter(Context c, List<Request> completedRequest) {
        this.c = c;
        this.completedRequest = completedRequest;
    }

    @NonNull
    @Override
    public RequestCompletedAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_completed_item, null);
        return new RequestCompletedAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestCompletedAdapter.MyHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        Log.i("RequestSize",String.valueOf(completedRequest.size()));
        return completedRequest.size();

    }

    public class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
