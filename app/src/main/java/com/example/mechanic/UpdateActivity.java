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
import android.widget.Toast;

import com.example.mechanic.model.Complaint;
import com.example.mechanic.model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.Calendar;
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


        Submit_Description = findViewById(R.id.Submit_Description);
        radio_group = findViewById(R.id.radio_group);
        submit_update = findViewById(R.id.submit_update);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        requestIdReference = firebaseDatabase.getReference("RequestId");

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

                Complaint complaint = Parcels.unwrap(getIntent().getParcelableExtra("complaint"));

                int selectedId = radio_group.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                status = radioButton.getText().toString();

                HashMap<String, Object> hashMap = new HashMap<>();

                Request request = new Request();

                request.setDescription(Submit_Description.getText().toString());

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                request.setGeneratedDate(day + "/" + month + "/" + year);

                request.setRequestId(requestIdValue);

                if (status.equals("Pending"))
                    request.setStatus(false);
                else if (status.equals("Completed"))
                    request.setStatus(true);

                Complaint tempComplaint = null;
                Request tempRequest = null;
                try {
                    tempComplaint = (Complaint) complaint.clone();
                    tempRequest = (Request) request.clone();
                    request.setComplaint(complaint);

                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                if (tempComplaint != null && tempRequest!=null) {

                    tempRequest.setComplaint(tempComplaint);
                    hashMap.put("/Requests/" + requestIdValue, tempRequest);
                }


                tempRequest = null;
                try {
                    tempRequest = (Request) request.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                if(tempRequest != null)
                {
                    tempRequest.getComplaint().setMechanic(null);
                    hashMap.put("/Users/Mechanic/" + user.getUid() + "/pendingRequests/" + requestIdValue, tempRequest);
                }



                String managerUid = complaint.getManager().getUid();
                tempRequest = null;
                try {
                    tempRequest = (Request) request.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                if (tempRequest != null) {
                    tempRequest.getComplaint().setManager(null);
                }
                hashMap.put("/Users/Manager/" + managerUid + "/pendingRequests/" + tempRequest.getRequestId(), tempRequest);
                hashMap.put("/Users/Manager/" + managerUid + "/pendingComplaints/" + complaint.getComplaintId() + "/status", 3);
                hashMap.put("/Complaints/" + complaint.getComplaintId() + "/status", 3);
                hashMap.put("/RequestId", requestIdValue + 1);

                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);

            }
        });

    }
}

