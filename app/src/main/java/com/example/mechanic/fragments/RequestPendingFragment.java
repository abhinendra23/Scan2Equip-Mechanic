package com.example.mechanic.fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.mechanic.adapters.RequestPendingAdapter;
import com.example.mechanic.model.CustomDialogBox;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestPendingFragment extends Fragment {


    RecyclerView s_recyclerView_pending_request;
    RequestPendingAdapter mRequestPendingAdapter;

    List<Request> pendingRequestObjectList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference requestReference, serviceManReference, pendingRequestListReference, responsibleManReference;

    FirebaseAuth auth;
    FirebaseUser user;

    String name;

    CustomDialogBox dialogBox;

    public RequestPendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.request_pending, container, false);


        s_recyclerView_pending_request = (RecyclerView)rootView.findViewById(R.id.s_recyclerView_pending_request);
        s_recyclerView_pending_request.setLayoutManager(new LinearLayoutManager(getActivity()));

        dialogBox = new CustomDialogBox(getActivity());
        dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogBox.show();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        pendingRequestObjectList = new ArrayList<Request>();

        mRequestPendingAdapter = new RequestPendingAdapter(getActivity().getApplicationContext(),pendingRequestObjectList);
        s_recyclerView_pending_request.setAdapter(mRequestPendingAdapter);

        firebaseDatabase =  FirebaseDatabase.getInstance();
        serviceManReference = firebaseDatabase.getReference("Users").child("ServiceMan").child(user.getUid());
        pendingRequestListReference = serviceManReference.child("pendingRequestList");
        responsibleManReference = firebaseDatabase.getReference("Users").child("ResponsibleMan");
        requestReference = firebaseDatabase.getReference("Requests");

        pendingRequestListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey().toString();

                requestReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Request request = new Request();
                        request = dataSnapshot.getValue(Request.class);



                        pendingRequestObjectList.add(0,request);
                        dialogBox.dismiss();
                        mRequestPendingAdapter.notifyDataSetChanged();


                        //Log.i("danda ghus gya",request.getComplaintId());
                        //Log.i("machine id", request.getComplaintMachineId());
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

