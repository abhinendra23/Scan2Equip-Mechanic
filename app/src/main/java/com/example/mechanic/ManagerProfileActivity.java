package com.example.mechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.MapNavigationActivity;
import com.example.mechanic.model.Complaint;
import com.example.mechanic.model.Manager;

import org.parceler.Parcels;

public class ManagerProfileActivity extends AppCompatActivity {

    ImageView profilepic;
    TextView managerName,managerEmail,managerPhone,managerDepartment;
    LinearLayout chat,navigation;
    Complaint complaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);
        final Manager manager = Parcels.unwrap(getIntent().getParcelableExtra("manager"));
        complaint = Parcels.unwrap(getIntent().getParcelableExtra("complaint"));
        profilepic = findViewById(R.id.circleImageView);
        managerName = findViewById(R.id.manager_name);
        managerEmail = findViewById(R.id.manager_email);
        managerPhone = findViewById(R.id.manager_phone);
        managerDepartment = findViewById(R.id.manager_department);
        managerName.setText(manager.getUserName());
        managerEmail.setText(manager.getEmail());
//        managerPhone.setText(manager.getPhoneNumber());

        chat = findViewById(R.id.ll_chat);
        navigation = findViewById(R.id.ll_navigate);

        Glide.with(this)
                .load(manager.getProfilePicLink())
                .fitCenter()
                .placeholder(R.drawable.profilepicdemo)
                .into(profilepic);


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ManagerProfileActivity.this, SMChatActivity.class);
                intent.putExtra("userid", complaint.getManager().getUid());
                intent.putExtra("complaintId", String.valueOf(complaint.getComplaintId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manager!=null && manager.getUserName()!=null) {
                    Intent intent = new Intent(ManagerProfileActivity.this, MapNavigationActivity.class);
                    intent.putExtra("manager", Parcels.wrap(manager));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }
            }
        });
    }
}