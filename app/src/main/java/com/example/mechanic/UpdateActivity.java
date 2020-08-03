package com.example.mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mechanic.dialogBox.RequestSentDialogBox;
import com.example.mechanic.model.Complaint;
import com.example.mechanic.model.Request;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

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
    Complaint complaint;

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

        complaint = Parcels.unwrap(getIntent().getParcelableExtra("complaint"));

        if(complaint.getStatus()==3)
        {
            submit_update.setText("Already updated");

            submit_update.setEnabled(false);
        }
        else
        {
            submit_update.setText("Submit");
            submit_update.setEnabled(true);
            submit_update.setText("Update");
        }

        submit_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                RequestSentDialogBox requestSentDialogBox = new RequestSentDialogBox(UpdateActivity.this);
                requestSentDialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                requestSentDialogBox.show();

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

                if (status.equals("Not yet"))
                    request.setStatus(false);
                else
                    request.setStatus(true);

                Complaint tempComplaint1 = null,tempComplaint2=null;
                Request tempRequest = null;
                try {
                    tempComplaint1 = (Complaint) complaint.clone();
                    tempComplaint2 = (Complaint) complaint.clone();
                    tempRequest = (Request) request.clone();
                    request.setComplaint(complaint);

                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                if (tempComplaint1 != null && tempRequest!=null) {

                    tempRequest.setComplaint(tempComplaint1);
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
                    Log.i("sudhanshu",tempRequest.getComplaint().getManager().getEmail());
                }



                String managerUid = complaint.getManager().getUid();
                request.setComplaint(tempComplaint2);
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

                //sending notification to manager
                FirebaseDatabase.getInstance().getReference("tokens").child(managerUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String token = (String) dataSnapshot.getValue();
                        Log.i("ankit token fetch checking",token);

                        HashMap<String,String> data = new HashMap<>();
                        data.put("description",request.getDescription());
                        data.put("status",status);
                        data.put("token",token);

                        FirebaseFunctions firebaseFunctions;
                        firebaseFunctions = FirebaseFunctions.getInstance();
                        firebaseFunctions.getHttpsCallable("sendRequest")
                                .call(data)
                                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                                    @Override
                                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                                        HashMap<String,String> hashMap = (HashMap<String, String>) httpsCallableResult.getData();
                                        if(hashMap.get("status").equals("successful")){
                                            Log.d("ankit successful","notification successfully sent");
                                            UpdateActivity.this.finish();
                                        }
                                        else{
                                            Log.d("ankit error occured",hashMap.get("status").toString());
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }
}

