package com.example.mechanic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.SMChatActivity;
import com.example.mechanic.UpdateActivity;
import com.example.mechanic.model.Complaint;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class PendingComplaintAdapter extends FirebaseRecyclerPagingAdapter<Complaint,PendingComplaintAdapter.MyHolder> {

    Context c;

    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    public PendingComplaintAdapter(@NonNull DatabasePagingOptions<Complaint> options,Context c) {
        super(options);

        this.c = c;
    }



    @NonNull
    @Override
    public PendingComplaintAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_complaint_item, null);
        return new PendingComplaintAdapter.MyHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull MyHolder viewHolder, int position, @NonNull Complaint model) {
        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

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
            pendingComplaintId = itemView.findViewById(R.id.pending_complaint_id);
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


//                    Complaint complaint = pendingComplaintList.get(getAdapterPosition());
////                    complaint.setExpanded(!complaint.isExpanded());
//                    notifyItemChanged(getAdapterPosition());

//
                }
            });

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
                    if (dataSnapshot != null) {
                        Complaint complaint = dataSnapshot.getValue(Complaint.class);
                        Toast.makeText(c, complaint.getMachine().getMachineId(), Toast.LENGTH_SHORT).show();
                    }
//                    Complaint complaint = pendingComplaintList.get(getAdapterPosition());
//                    Intent i = new Intent(c, UpdateActivity.class);
//                    i.putExtra("generatorUid",complaint.getManager().getUserName());
//                    i.putExtra("complaintId",complaint.getComplaintId());
//                    i.putExtra("generatorName",complaint.getManager().getUserName());
//                    i.putExtra("servicemanName", complaint.getMechanic().getUserName());
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    c.getApplicationContext().startActivity(i);
                }
            });

            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
                    Complaint complaint = null;
                    if (dataSnapshot != null) {
                        complaint = dataSnapshot.getValue(Complaint.class);
                    }
                    Intent intent = new Intent(c, SMChatActivity.class);
                    intent.putExtra("userid", complaint.getManager().getUid());
                    intent.putExtra("complaintId", String.valueOf(complaint.getComplaintId()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(intent);

                }
            });

            statusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Complaint complaint = pendingComplaintList.get(getAdapterPosition());
//                    Intent intent = new Intent(c, RMRequestStepIndicator.class);
//                    intent.putExtra("status", complaint.getStatus());
//                    intent.putExtra("generated date", complaint.getComplaintGeneratedDate());
//                    intent.putExtra("serviceman", complaint.getServicemanName());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    c.getApplicationContext().startActivity(intent);
                }
            });
        }

        public void bind(Complaint model)
        {
            pendingComplaintDescription.setText(model.getDescription());
            pendingComplaintGeneratorName.setText(model.getManager().getUserName());
            pendingComplaintId.setText(String.valueOf((int) model.getComplaintId()));
            pendingComplaintMachineId.setText(model.getMachine().getMachineId());

//        boolean isExpanded = pendingComplaintList.get(position).isExpanded();
            boolean isExpanded = true;
            ll_hide.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

    }
}
