package com.example.mechanic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mechanic.adapters.ShowDetailsAdapter;
import com.example.mechanic.model.PastRecord;
import com.example.mechanic.model.Request;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ShowDetailsAdapter showDetailsAdapter;
    FloatingActionButton floatingActionButton;
    String generationCode;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference historyReference,pastRecordsReference;
    SwipeRefreshLayout swipeRefereshLayout;
    ShimmerFrameLayout shimmerFrameLayout;

    List<PastRecord> pastRecords;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        final Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_machine_type);
        toolbar.setTitleTextAppearance(this,R.style.TitleTextAppearance);


        generationCode = getIntent().getStringExtra("generationCode");


        firebaseDatabase = FirebaseDatabase.getInstance();

        historyReference = firebaseDatabase.getReference("Machines").child(generationCode).child("pastRecords");


        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        // shimmerFrameLayout.startShimmerAnimation();

        swipeRefereshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefereshLayout.setColorSchemeColors(Color.BLUE);

                swipeRefereshLayout.setRefreshing(false);

            }
        });

        Query baseQuery = firebaseDatabase.getReference("Machines").child(generationCode).child("pastRecords");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<PastRecord> options = new DatabasePagingOptions.Builder<PastRecord>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,PastRecord.class)
                .build();

        showDetailsAdapter = new ShowDetailsAdapter(options, ShowDetailsActivity.this);
        recyclerView.setAdapter(showDetailsAdapter);
        showDetailsAdapter.startListening();

    }
}
