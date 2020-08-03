package com.example.mechanic;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mechanic.dialogBox.CustomDialogBox;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import xyz.hasnat.sweettoast.SweetToast;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginButton;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference serviceManReference;

    CustomDialogBox customDialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        TextView ScanBC = findViewById(R.id.scanBC);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(LoginActivity.this, BottomNavigationActivity.class));
            finish();
        }

        ScanBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, BarCodeLoginActivity.class);
                overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
                startActivityForResult(i, 32);
            }
        });

        customDialogBox = new CustomDialogBox(LoginActivity.this);
        customDialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        loginButton = findViewById(R.id.loginButton);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        firebaseDatabase = FirebaseDatabase.getInstance();
        serviceManReference = firebaseDatabase.getReference("Users").child("Mechanic");


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                email = email.trim();
                if(email.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Email should be valid", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<6)
                {
                    Toast.makeText(LoginActivity.this, "Password should be atleast 6 character long", Toast.LENGTH_SHORT).show();
                }
                else {
                    customDialogBox.show();
                    login(email, password);

                }

            }
        });
    }

    private void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String newToken = instanceIdResult.getToken();
                                    FirebaseDatabase.getInstance().getReference("tokens/" +
                                            mAuth.getCurrentUser().getUid()).setValue(newToken);

                                }
                            });

                            FirebaseUser user = mAuth.getCurrentUser();
                            user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                @Override
                                public void onSuccess(GetTokenResult getTokenResult) {
                                    try {
                                        boolean isMechanic = (boolean) getTokenResult.getClaims().get("mechanic");
                                        if(isMechanic)
                                        {
                                            customDialogBox.dismiss();
                                            SweetToast.success(getApplicationContext(),"Login Successfully");
                                            Intent i = new Intent(LoginActivity.this,BottomNavigationActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else
                                        {
                                            customDialogBox.dismiss();
                                            SweetToast.error(getApplicationContext(),"You Entered wrong Id or password");
                                        }

                                    }
                                    catch (Exception e)
                                    {
                                        customDialogBox.dismiss();
                                        SweetToast.error(getApplicationContext(),"Error");
                                    }

                                }
                            });

                        } else {
                            customDialogBox.dismiss();
                            SweetToast.error(getApplicationContext(),"You Entered wrong Id or password");

                        }
                    }
                });
    }
    public void onLoginClick(View View){
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();
    }

    public void RegisterClick(View view)
    {
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();
    }

    public void onForgetPassword(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_one_input,null);

        final EditText m_Text = (EditText)mView.findViewById(R.id.D1I_input);
        Button confirm = (Button)mView.findViewById(R.id.D1I_confirm);
        Button cancel = (Button)mView.findViewById(R.id.D1I_cancel);

        builder.setView(mView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        final CustomDialogBox customDialogBox1 = new CustomDialogBox(this);
        customDialogBox1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                customDialogBox1.show();
                String text = m_Text.getText().toString();
                alertDialog.dismiss();
                mAuth.sendPasswordResetEmail(text).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Email has been sent to your Registered Email", Toast.LENGTH_SHORT).show();
                            customDialogBox1.dismiss();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "AA gyi dikkat", Toast.LENGTH_SHORT).show();
                            customDialogBox1.dismiss();
                        }


                    }
                });
            }
        });

        alertDialog.show();

    }


}
