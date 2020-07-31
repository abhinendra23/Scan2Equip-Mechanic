package com.example.mechanic.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mechanic.R;
import com.example.mechanic.adapters.RecentChatAdapter;
import com.example.mechanic.model.Complaint;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RecentChatFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ConstraintLayout emptyView;
    FirebaseAuth auth;
    FirebaseUser user;

    ShimmerFrameLayout shimmerFrameLayout;
    public RecentChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_chat, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_container);
        shimmerFrameLayout.startShimmer();
        recyclerView = view.findViewById(R.id.recent_chat_rv);
        recyclerView.setVisibility(View.INVISIBLE); // this will be set to visible recent chat adapter after shimmer effect stops.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Query baseQuery = firebaseDatabase.getReference("Users/Mechanic/"+user.getUid()+"/pendingComplaints");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Complaint> options = new DatabasePagingOptions.Builder<Complaint>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Complaint.class)
                .build();
        RecentChatAdapter recentChatAdapter = new RecentChatAdapter(options,getActivity().getApplicationContext(),view);
        recyclerView.setAdapter(recentChatAdapter);
        recentChatAdapter.startListening();
        return view;
    }
}