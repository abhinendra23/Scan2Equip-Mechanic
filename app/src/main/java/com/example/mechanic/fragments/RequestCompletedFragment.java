package com.example.mechanic.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.adapters.RequestCompletedAdapter;
import com.example.mechanic.model.Mechanic;
import com.example.mechanic.model.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.OnDisconnect;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RequestCompletedFragment extends Fragment {


    RecyclerView s_recyclerView_completed_request;
    RequestCompletedAdapter requestCompletedAdapter;
    LinearLayout nothing;


    FirebaseDatabase firebaseDatabase;

    FirebaseAuth auth;
    FirebaseUser user;

    public RequestCompletedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.request_completed, container, false);

        s_recyclerView_completed_request = (RecyclerView)rootview.findViewById(R.id.s_recyclerView_completed_request);
        nothing = rootview.findViewById(R.id.EmptyList2);
        s_recyclerView_completed_request.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        Query baseQuery = firebaseDatabase.getReference("Users").child("Mechanic").child(user.getUid()).child("completedRequests");

        DatabaseReference reference1 = firebaseDatabase.getReference().child("Users").child("Mechanic").child(user.getUid()).child("completedRequests");

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    nothing.setVisibility(View.VISIBLE);
                    s_recyclerView_completed_request.setVisibility(View.GONE);
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

        DatabasePagingOptions<Request> options = new DatabasePagingOptions.Builder<Request>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Request.class)
                .build();



        requestCompletedAdapter = new RequestCompletedAdapter(options, getActivity().getApplicationContext());
        s_recyclerView_completed_request.setAdapter(requestCompletedAdapter);
        requestCompletedAdapter.startListening();

        return rootview;
    }
}
