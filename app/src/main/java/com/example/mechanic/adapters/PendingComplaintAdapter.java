package com.example.mechanic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.SMChatActivity;
import com.example.mechanic.UpdateActivity;
import com.example.mechanic.model.Complaint;

import java.util.List;

public class PendingComplaintAdapter extends RecyclerView.Adapter<PendingComplaintAdapter.MyHolder> {

    Context c;
    public List<Complaint> pendingComplaintList;

    public PendingComplaintAdapter(Context c, List<Complaint> pendingComplaint) {
        this.c = c;
        this.pendingComplaintList = pendingComplaint;
    }

    public PendingComplaintAdapter(Context c) {
        this.c = c;
    }

    public void setPendingComplaintList(List<Complaint> pendingComplaintList) {
        this.pendingComplaintList = pendingComplaintList;
    }

    @NonNull
    @Override
    public PendingComplaintAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_complaint_item, null);
        return new PendingComplaintAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingComplaintAdapter.MyHolder myholder, int position) {

        //myholder.pendingComplaintDate.setText(pendingComplaintList.get(position).getComplaintGeneratedDate());
        myholder.pendingComplaintDescription.setText(pendingComplaintList.get(position).getComplaintDescription());
        myholder.pendingComplaintGeneratorName.setText(pendingComplaintList.get(position).getGeneratorName());
        myholder.pendingComplaintId.setText(pendingComplaintList.get(position).getComplaintId());
        myholder.pendingComplaintMachineId.setText(pendingComplaintList.get(position).getComplaintMachineId());

        boolean isExpanded = pendingComplaintList.get(position).isExpanded();
        myholder.ll_hide.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return pendingComplaintList.size();
    }





    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView pendingComplaintDate, pendingComplaintId, pendingComplaintGeneratorName, pendingComplaintDescription, pendingComplaintMachineId;
        CardView cardView;
        LinearLayout ll_hide;
        Button updateButton ,chatButton, statusButton;

        public MyHolder(@NonNull final View itemView)
        {
            super(itemView);

            pendingComplaintDate = itemView.findViewById(R.id.sm_pending_complaint_date);
            pendingComplaintId = itemView.findViewById(R.id.sm_pending_complaint_Id);
            pendingComplaintDescription = itemView.findViewById(R.id.sm_pending_complaint_description);
            pendingComplaintGeneratorName = itemView.findViewById(R.id.sm_pending_complaint_genratorName);
            updateButton = itemView.findViewById(R.id.update_button);
            pendingComplaintMachineId = itemView.findViewById((R.id.sm_pending_complaint_machine_id));
            chatButton = itemView.findViewById(R.id.sm_chat_button);
            statusButton = itemView.findViewById(R.id.sm_status_button);



            cardView = itemView.findViewById(R.id.cardview12);
            ll_hide = itemView.findViewById(R.id.ll_hide12);
//            public void onClick()
//            {
//
//            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Complaint complaint = pendingComplaintList.get(getAdapterPosition());
                    complaint.setExpanded(!complaint.isExpanded());
                    notifyItemChanged(getAdapterPosition());

//
                }
            });

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Complaint complaint = pendingComplaintList.get(getAdapterPosition());
                    Intent i = new Intent(c, UpdateActivity.class);
                    i.putExtra("generatorUid",complaint.getComplaintGenerator());
                    i.putExtra("complaintId",complaint.getComplaintId());
                    i.putExtra("generatorName",complaint.getGeneratorName());
                    i.putExtra("servicemanName", complaint.getServicemanName());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(i);
                }
            });

            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Complaint complaint = pendingComplaintList.get(getAdapterPosition());
                    Intent intent = new Intent(c, SMChatActivity.class);
                    intent.putExtra("userid", complaint.getComplaintGenerator());
                    intent.putExtra("complaintId", complaint.getComplaintId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(intent);

                }
            });

            statusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Complaint complaint = pendingComplaintList.get(getAdapterPosition());
                    Intent intent = new Intent(c, RMRequestStepIndicator.class);
                    intent.putExtra("status", complaint.getStatus());
                    intent.putExtra("generated date", complaint.getComplaintGeneratedDate());
                    intent.putExtra("serviceman", complaint.getServicemanName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(intent);
                }
            });
        }

    }
}
