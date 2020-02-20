package com.example.mechanic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.mechanic.adapters.PendingComplaintAdapter;
import com.example.mechanic.model.Complaint;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingComplaintsActivity extends AppCompatActivity {

    RecyclerView recyclerView_complaints;
    PendingComplaintAdapter myPendingComplaintAdapter;

    List<String> pendingComplaintList;

    List<Complaint> pendingComplaintObjectList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, pendingComplaintListReference, complaintReference, OthersReference;

    FirebaseAuth auth;
    FirebaseUser user;

    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complaints);


        recyclerView_complaints = findViewById(R.id.recyclerView_complaints);
        recyclerView_complaints.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        pendingComplaintList = new ArrayList<String>();
        pendingComplaintObjectList = new ArrayList<Complaint>();

        myPendingComplaintAdapter = new PendingComplaintAdapter(getApplicationContext(),pendingComplaintObjectList);
        recyclerView_complaints.setAdapter(myPendingComplaintAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users").child("ServiceMan").child(user.getUid());
        pendingComplaintListReference = reference.child("pendingComplaintList");
        OthersReference = firebaseDatabase.getReference("Users").child("ResponsibleMan");
        complaintReference = firebaseDatabase.getReference("Complaints");


        pendingComplaintListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();

                Log.i("vikas key", key);
                complaintReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Complaint complaint = new Complaint();
                        complaint = dataSnapshot.getValue(Complaint.class);
                        pendingComplaintObjectList.add(0,complaint);
                        myPendingComplaintAdapter.notifyDataSetChanged();


                        //Log.i("machine id", complaint.getComplaintMachineId());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
