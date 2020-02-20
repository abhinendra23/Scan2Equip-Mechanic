package com.example.mechanic.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.adapters.RequestCompletedAdapter;
import com.example.mechanic.model.Request;
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


public class RequestCompletedFragment extends Fragment {


    public RequestCompletedFragment() {
        // Required empty public constructor
    }

    FirebaseDatabase firebaseDatabase;
    DatabaseReference completedRequestList;

    FirebaseUser user;
    List<Request> completedRequestObjectList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.request_completed, container, false);

        RecyclerView s_recyclerView_completed_request = (RecyclerView)rootview.findViewById(R.id.s_recyclerView_completed_request);
        s_recyclerView_completed_request.setLayoutManager(new LinearLayoutManager(getActivity()));

        user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();

        completedRequestList = firebaseDatabase
                .getReference("Users")
                .child("ServiceMan")
                .child(user.getUid())
                .child("completedRequestList");

        completedRequestObjectList = new ArrayList<>();

        final RequestCompletedAdapter adapter = new RequestCompletedAdapter(getActivity().getApplicationContext(), completedRequestObjectList);
        s_recyclerView_completed_request.setAdapter(adapter);

        completedRequestList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();

                final DatabaseReference Request = firebaseDatabase
                        .getReference("Requests")
                        .child(key);

                Request.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Request request = dataSnapshot.getValue(Request.class);
                        completedRequestObjectList.add(request);
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


        return rootview;
    }

}
