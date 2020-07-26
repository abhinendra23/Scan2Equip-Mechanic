package com.example.mechanic;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.adapters.ShowDetailsAdapter;
import com.example.mechanic.adapters.ShowHistoryDetailsAdapter;
import com.example.mechanic.model.Complaint;
import com.example.mechanic.model.Machine;
import com.example.mechanic.model.PastRecord;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;

public class GetMachineDetailsActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference machineReference, complaintIdReference, serviceManListReference, responsibleReference,complaintReference;

    FirebaseAuth auth;
    FirebaseUser user;

    String generationCode;
    Machine machine;
    ShowHistoryDetailsAdapter showHistoryDetailsAdapter;

    ImageView QRCodeImage;
    Button show_history;
    Button generateComplaint;

    String machineId;

    Complaint complaint;

    String complaintIdValue;
    String description;

    TextView company_name;
    TextView type,machineModelNumber,price,machineDetailsDepartment, machineDetailsServiceTime,machineDetailsInstallationDate,generator_name;
    TextView machineDetailsSerialNo;
    RecyclerView recyclerView;
    LinearLayoutManager HorizontalLayout;
    ScrollView ScrollViewHistory;
    TextView NoMachineHistory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_machine_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        machineModelNumber = findViewById(R.id.machineModelNumber);
        price = findViewById(R.id.price);
        machineDetailsServiceTime = findViewById(R.id.machineDetailsServiceTime);
        machineDetailsInstallationDate = findViewById(R.id.machineDetailsInstallationDate);
        generator_name = findViewById(R.id.generator_name);
        machineDetailsSerialNo = findViewById(R.id.machineDetailsSerialNo);
        company_name = findViewById(R.id.company_name);
        type = findViewById(R.id.machine_type);
        machineDetailsDepartment = findViewById(R.id.machineDetailsDepartment);
        show_history = findViewById(R.id.show_history);
        ScrollViewHistory = findViewById(R.id.scrollViewHistory);
        NoMachineHistory = findViewById(R.id.xyz1);




        description = new String("");


        generationCode = getIntent().getStringExtra("generationCode");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        machineReference = firebaseDatabase.getReference("Machines").child(generationCode);
        complaintIdReference = firebaseDatabase.getReference("ComplaintId");
        serviceManListReference = firebaseDatabase.getReference("Users").child("Mechanic");
        //responsibleReference = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid());
        complaintReference = firebaseDatabase.getReference("Complaints");



        machineReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                machine = dataSnapshot.getValue(Machine.class);
                //Toast.makeText(GetMachineDetailsActivity.this, machine.getDepartment(), Toast.LENGTH_SHORT).show();

                machineDetailsSerialNo.setText(machine.getSerialNumber());
                machineDetailsDepartment.setText(machine.getDepartment());
                machineDetailsServiceTime.setText(machine.getServiceTime()+" months");
                machineDetailsInstallationDate.setText(machine.getDateOfInstallation());
                generator_name.setText(machine.getManager().getUserName());
                company_name.setText(machine.getCompany());
                price.setText(String.valueOf(machine.getPrice()));
                type.setText(machine.getType());
                machineModelNumber.setText(machine.getModelNumber());
                machineId = machine.getMachineId();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        complaintIdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaintIdValue = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        recyclerView=findViewById(R.id.machine_history_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        HorizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        Query baseQuery1 = firebaseDatabase.getReference("Machines").child(generationCode).child("pastRecords");

        DatabaseReference reference1 = firebaseDatabase.getReference().child("Machines").child(generationCode).child("pastRecords");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    show_history.setVisibility(View.GONE);
                    ScrollViewHistory.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    NoMachineHistory.setVisibility(View.VISIBLE);
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

        DatabasePagingOptions<PastRecord> options = new DatabasePagingOptions.Builder<PastRecord>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery1, config, PastRecord.class)
                .build();

        showHistoryDetailsAdapter = new ShowHistoryDetailsAdapter(options, GetMachineDetailsActivity.this);
        recyclerView.setAdapter(showHistoryDetailsAdapter);
        showHistoryDetailsAdapter.startListening();



        show_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetMachineDetailsActivity.this, ShowDetailsActivity.class);
                i.putExtra("generationCode",generationCode);
                startActivity(i);
            }
        });



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
