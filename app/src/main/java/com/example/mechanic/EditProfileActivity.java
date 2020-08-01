package com.example.mechanic;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mechanic.model.Complaint;
import com.example.mechanic.model.Manager;
import com.example.mechanic.model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    ImageView save,cancel;
    CircleImageView profile;
    EditText name, phone, staff, jobTitle, department, address;
    DatabaseReference mechanicReference, complaintReference, requestReference, managerReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        name = findViewById(R.id.edit_profile_name);
        phone = findViewById(R.id.edit_profile_phone);
        staff = findViewById(R.id.staff_number);
        department = findViewById(R.id.department);
        save = findViewById(R.id.save_button);
        cancel = findViewById(R.id.close_btn);
        profile = findViewById(R.id.profilepic);
        profile.setColorFilter(ContextCompat.getColor(
                this,
                R.color.grey_overlay),
                PorterDuff.Mode.SRC_OVER
        );

        firebaseDatabase = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String newName, newPhone, newStaff, newJobTitle, newDepartment, newAddress;
                newName = name.getText().toString();
                newPhone = phone.getText().toString();
                newStaff = staff.getText().toString();
                newDepartment = department.getText().toString();

                mechanicReference = firebaseDatabase.getReference("Users").child("Mechanic").child(user.getUid());
                HashMap<String, Object> hashMap = new HashMap<>();
                if(!newName.isEmpty())
                    hashMap.put("/Users/Mechanic/" + user.getUid() + "/userName", newName);
                if(!newPhone.isEmpty())
                    hashMap.put("/Users/Mechanic/" + user.getUid() + "/phone", newPhone);
                if(!newStaff.isEmpty())
                    hashMap.put("/Users/Mechanic/" + user.getUid() + "/empId", newStaff);
                if(!newDepartment.isEmpty())
                    hashMap.put("/Users/Mechanic/" + user.getUid() + "/department", newDepartment);

                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);

                complaintReference = firebaseDatabase.getReference("Complaints");
                complaintReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot complaint : dataSnapshot.getChildren())
                        {
                            Complaint complaint1 = complaint.getValue(Complaint.class);
                            if(complaint1.getMechanic().getUid().equals(user.getUid()))
                            {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                if(!newName.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId()+ "/mechanic/userName", newName);
                                if(!newPhone.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId()+ "/mechanic/phone", newPhone);
                                if(!newStaff.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId()+ "/mechanic/empId", newStaff);
                                if(!newDepartment.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId()+ "/mechanic/department", newDepartment);

                                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                requestReference = firebaseDatabase.getReference("Requests");
                requestReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot request : dataSnapshot.getChildren())
                        {
                            Request request1 = request.getValue(Request.class);
                            if(request1.getComplaint().getMechanic().getUid().equals(user.getUid()))
                            {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                if(!newName.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId()+ "/complaint/mechanic/userName", newName);
                                if(!newPhone.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId()+ "/complaint/mechanic/phone", newPhone);
                                if(!newStaff.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId()+ "/complaint/mechanic/empId", newStaff);
                                if(!newDepartment.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId()+ "/complaint/mechanic/department", newDepartment);

                                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                managerReference = firebaseDatabase.getReference("Users").child("Manager");
                managerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot manager : dataSnapshot.getChildren())
                        {
                            final Manager manager1 = manager.getValue(Manager.class);
                            DatabaseReference pendingComplaint = firebaseDatabase.getReference("Users").child("Manager").child(manager1.getUid()).child("pendingComplaints");
                            pendingComplaint.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot complaint : dataSnapshot.getChildren())
                                    {
                                        Complaint complaint1 = complaint.getValue(Complaint.class);
                                        if(complaint1.getMechanic().getUid().equals(user.getUid()))
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            if(!newName.isEmpty())
                                                hashMap.put("/Users/Manager/"+ manager1.getUid() +"/pendingComplaints/" + complaint1.getComplaintId()+ "/mechanic/userName", newName);
                                            if(!newPhone.isEmpty())
                                                hashMap.put("/Users/Manager/"+ manager1.getUid() +"/pendingComplaints/" + complaint1.getComplaintId()+ "/mechanic/phone", newPhone);
                                            if(!newStaff.isEmpty())
                                                hashMap.put("/Users/Manager/"+ manager1.getUid() +"/pendingComplaints/" + complaint1.getComplaintId()+ "/mechanic/empId", newStaff);
                                            if(!newDepartment.isEmpty())
                                                hashMap.put("/Users/Manager/"+ manager1.getUid() +"/pendingComplaints/" + complaint1.getComplaintId()+ "/mechanic/department", newDepartment);

                                            FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference completedComplaint = firebaseDatabase.getReference("Users").child("Manager").child(manager1.getUid()).child("completedComplaints");
                            completedComplaint.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot complaint : dataSnapshot.getChildren())
                                    {
                                        Complaint complaint1 = complaint.getValue(Complaint.class);
                                        if(complaint1.getMechanic().getUid().equals(user.getUid()))
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            if(!newName.isEmpty())
                                                hashMap.put("/Users/Manager/"+ manager1.getUid() +"/completedComplaints/" + complaint1.getComplaintId()+ "/mechanic/userName", newName);
                                            if(!newPhone.isEmpty())
                                                hashMap.put("/Users/Manager/"+ manager1.getUid() +"/completedComplaints/" + complaint1.getComplaintId()+ "/mechanic/phone", newPhone);
                                            if(!newStaff.isEmpty())
                                                hashMap.put("/Users/Manager/"+ manager1.getUid() +"/completedComplaints/" + complaint1.getComplaintId()+ "/mechanic/empId", newStaff);
                                            if(!newDepartment.isEmpty())
                                                hashMap.put("/Users/Manager/"+ manager1.getUid() +"/completedComplaints/" + complaint1.getComplaintId()+ "/mechanic/department", newDepartment);

                                            FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference pendingRequest = firebaseDatabase.getReference("Users").child("Manager").child(manager1.getUid()).child("pendingRequests");
                            pendingRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot request : dataSnapshot.getChildren())
                                    {
                                        Request request1 = request.getValue(Request.class);
                                        if(request1.getComplaint().getMechanic().getUid().equals(user.getUid()))
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            if(!newName.isEmpty())
                                                hashMap.put("/Users/Manager/" + manager1.getUid() + "/pendingRequests/" + request1.getRequestId()+ "/complaint/mechanic/userName", newName);
                                            if(!newPhone.isEmpty())
                                                hashMap.put("/Users/Manager/" + manager1.getUid() + "/pendingRequests/" + request1.getRequestId()+ "/complaint/mechanic/phone", newPhone);
                                            if(!newStaff.isEmpty())
                                                hashMap.put("/Users/Manager/" + manager1.getUid() + "/pendingRequests/" + request1.getRequestId()+ "/complaint/mechanic/empId", newStaff);
                                            if(!newDepartment.isEmpty())
                                                hashMap.put("/Users/Manager/" + manager1.getUid() + "/pendingRequests/" + request1.getRequestId()+ "/complaint/mechanic/department", newDepartment);

                                            FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}