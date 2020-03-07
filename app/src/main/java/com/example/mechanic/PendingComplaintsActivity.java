package com.example.mechanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mechanic.adapters.PendingComplaintAdapter;
import com.example.mechanic.model.Complaint;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class PendingComplaintsActivity extends AppCompatActivity {

    RecyclerView recyclerView_complaints;
    PendingComplaintAdapter pendingComplaintAdapter;

    FirebaseDatabase firebaseDatabase;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complaints);


        recyclerView_complaints = findViewById(R.id.recyclerView_complaints);
        recyclerView_complaints.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        Query baseQuery = firebaseDatabase.getReference("Users").child("Mechanic").child(user.getUid()).child("pendingComplaints");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Complaint> options = new DatabasePagingOptions.Builder<Complaint>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Complaint.class)
                .build();

        pendingComplaintAdapter = new PendingComplaintAdapter(options,PendingComplaintsActivity.this);
        recyclerView_complaints.setAdapter(pendingComplaintAdapter);
        pendingComplaintAdapter.startListening();




    }
}
