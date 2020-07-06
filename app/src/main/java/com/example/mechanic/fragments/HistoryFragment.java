package com.example.mechanic.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.adapters.HistoryAdapter;
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

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    HistoryAdapter adapter;

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference complaintReference, responsibleManReference, pendingComplaintListReference;
    LinearLayout nothing;

    List<Complaint> completedComplaintObjectList;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.historyFragmentRecyclerView);
        nothing = rootView.findViewById(R.id.EmptyList3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        completedComplaintObjectList = new ArrayList<>();

        adapter = new HistoryAdapter(getActivity().getApplicationContext(),completedComplaintObjectList);
        recyclerView.setAdapter(adapter);

        firebaseDatabase =  FirebaseDatabase.getInstance();
        responsibleManReference = firebaseDatabase.getReference("Users").child("Mechanic").child(user.getUid());
        pendingComplaintListReference = responsibleManReference.child("completedComplaints");
        complaintReference = firebaseDatabase.getReference("Complaints");

        DatabaseReference reference1 = responsibleManReference;

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    nothing.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        pendingComplaintListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();

                complaintReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Complaint pendingComplaint = new Complaint();
                        pendingComplaint = dataSnapshot.getValue(Complaint.class);

                        completedComplaintObjectList.add(0,pendingComplaint);
                        adapter.notifyDataSetChanged();

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

        return rootView;
    }
}
