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
    DatabaseReference managerReference, complaintReference, requestReference, mechanicReference, machineReference;
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