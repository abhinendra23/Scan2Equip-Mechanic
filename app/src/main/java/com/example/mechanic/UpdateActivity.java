package com.example.mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mechanic.model.Complaint;
import com.example.mechanic.model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class UpdateActivity extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference requestIdReference, requestReference, MechanicReference, ManagerReference;

    FirebaseAuth auth;
    FirebaseUser user;

    EditText Submit_Description;
    RadioGroup radio_group;
    RadioButton radioButton;
    String status;
    Button submit_update;

    long requestIdValue;
    //String generatorUid, complaintId, responsibleName, servicemanName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        Submit_Description = (EditText) findViewById(R.id.Submit_Description);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        submit_update = (Button) findViewById(R.id.submit_update);

//        generatorUid = getIntent().getStringExtra("generatorUid");
//        complaintId = getIntent().getStringExtra("complaintId");
//        responsibleName = getIntent().getStringExtra("generatorName");
//        servicemanName = getIntent().getStringExtra("servicemanName");

        Complaint complaint = Parcels.unwrap(getIntent().getParcelableExtra("complaint"));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        requestIdReference = firebaseDatabase.getReference("RequestId");
        requestReference = firebaseDatabase.getReference("Requests");
        MechanicReference = firebaseDatabase.getReference("Users").child("Mechanic").child(user.getUid());
        ManagerReference = firebaseDatabase.getReference("Users").child("Manager").child(complaint.getManager().getUid());


        requestIdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestIdValue = (long) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        submit_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int selectedId = radio_group.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                status = radioButton.getText().toString();

                Request request = new Request();
//                request.setServiceMan(user.getUid());
//                request.setDescription(Submit_Description.getText().toString());
//                Log.i("status", status);
//                if(status.equals("Pending"))
//                    request.setStatus(false);
//                else if(status.equals("Completed"))
//                    request.setStatus(true);
//                request.setResponsible(generatorUid);
//                request.setComplaintId(complaintId);
//                request.setRequestid(requestIdValue);
//                request.setResponsiblemanName(responsibleName);
//                request.setServicemanName(servicemanName);

//                MechanicReference.child("pendingRequests").push().setValue(requestIdValue);
//                ManagerReference.child("pendingRequests").push().setValue(requestIdValue);

                HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                updateDatabaseValue.put("/Users/ServiceMan/"+user.getUid()+"/pendingRequestList/"+requestIdValue,"true");
                updateDatabaseValue.put("/Users/ResponsibleMan/"+generatorUid+ "/pendingRequestList/"+requestIdValue,"true");
                updateDatabaseValue.put("/Requests/"+requestIdValue,request);
                updateDatabaseValue.put("/RequestId",String.valueOf(Integer.parseInt(requestIdValue)+1));
                updateDatabaseValue.put("/Complaints/"+complaintId+"/status",3);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

//                requestReference.child(requestIdValue).setValue(request);
//                requestIdReference.setValue(String.valueOf(Integer.parseInt(requestIdValue)+1));



            }
        });

    }
}

