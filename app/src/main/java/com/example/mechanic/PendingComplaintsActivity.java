package com.example.mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mechanic.adapters.PendingComplaintAdapter;
import com.example.mechanic.model.Complaint;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


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
        Toolbar toolbar=findViewById(R.id.toolbar);
        final LinearLayout nothing = findViewById(R.id.EmptyList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextAppearance(this,R.style.TitleTextAppearance);
        recyclerView_complaints = findViewById(R.id.recyclerView_complaints);
        recyclerView_complaints.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        Query baseQuery = firebaseDatabase.getReference("Users").child("Mechanic").child(user.getUid()).child("pendingComplaints");

        DatabaseReference reference1 = firebaseDatabase.getReference().child("Users").child("Mechanic").child(user.getUid()).child("pendingComplaints");

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    nothing.setVisibility(View.VISIBLE);
                    recyclerView_complaints.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
