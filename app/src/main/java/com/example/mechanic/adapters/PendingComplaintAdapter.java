package com.example.mechanic.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.ManagerProfileActivity;
import com.example.mechanic.R;
import com.example.mechanic.RequestStepIndicator;
import com.example.mechanic.SMChatActivity;
import com.example.mechanic.UpdateActivity;
import com.example.mechanic.model.Complaint;
import com.example.mechanic.model.Manager;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.parceler.Parcels;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingComplaintAdapter extends FirebaseRecyclerPagingAdapter<Complaint,PendingComplaintAdapter.MyHolder> {

    Context c;
    private final int[] mColors = {R.color.list_color_1,R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};
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
        int bgColor = ContextCompat.getColor(c, mColors[position % 12]);
        viewHolder.cardView.setCardBackgroundColor(bgColor);
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
        ImageView expand;
        CircularImageView managerPic;



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
            expand = itemView.findViewById(R.id.dropdown);
            cardView = itemView.findViewById(R.id.cardview12);
            ll_hide = itemView.findViewById(R.id.ll_hide12);
            ll_hide.setVisibility(View.GONE);
            managerPic = itemView.findViewById(R.id.manager_img);



            managerPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
                    Manager manager = null;
                    Complaint complaint = null;
                    if (dataSnapshot != null) {
                        complaint = dataSnapshot.getValue(Complaint.class);
                        manager = complaint.getManager();
                    }
                    if(manager!=null && manager.getUserName()!=null && complaint != null) {
                        Intent intent = new Intent(c, ManagerProfileActivity.class);
                        intent.putExtra("manager", Parcels.wrap(manager));
                        intent.putExtra("complaint",Parcels.wrap(complaint));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.getApplicationContext().startActivity(intent);
                    }
                }
            });


//            public void onClick()
//            {
//
//            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    Complaint complaint = pendingComplaintList.get(getAdapterPosition());
//                    complaint.setExpanded(!complaint.isExpanded());
//                    notifyItemChanged(getAdapterPosition());
                    if(ll_hide.getVisibility()==View.GONE)
                    {
                        ll_hide.setVisibility(View.VISIBLE);

                        expand.setRotation(360);
                    }
                    else
                    {
                        ll_hide.setVisibility(View.GONE);
                        expand.setRotation(-90);
                    }
//
                }
            });


            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
                    Complaint complaint = null;
                    if (dataSnapshot != null) {
                        complaint = dataSnapshot.getValue(Complaint.class);
//                        Toast.makeText(c, complaint.getManager().getUid(), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(c, UpdateActivity.class);
//                        i.putExtra("complaint", Parcels.wrap(complaint));
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        c.getApplicationContext().startActivity(i);
                    }

                    FirebaseDatabase firebaseDatabase;
                    DatabaseReference complaintReference;

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    complaintReference = firebaseDatabase.getReference("Complaints").child(String.valueOf(complaint.getComplaintId()));

                    complaintReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Complaint complaint1 = dataSnapshot.getValue(Complaint.class);
                            Toast.makeText(c, complaint1.getManager().getUid(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(c, UpdateActivity.class);
                            i.putExtra("complaint", Parcels.wrap(complaint1));
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            c.getApplicationContext().startActivity(i);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


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



//                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
//                    Complaint complaint = null;
//                    if (dataSnapshot != null) {
//                        complaint = dataSnapshot.getValue(Complaint.class);
//                    }
//                    Intent intent = new Intent(c, RequestStepIndicator.class);
//                    intent.putExtra("complaint", Parcels.wrap(complaint));
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
       //     ll_hide.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

    }
}
