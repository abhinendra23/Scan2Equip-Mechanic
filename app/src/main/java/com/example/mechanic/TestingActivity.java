package com.example.mechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mechanic.dialogBox.RequestSentDialogBox;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        Button dialogBox = findViewById(R.id.dialogbox);
        dialogBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RequestSentDialogBox(TestingActivity.this).show();
            }
        });
    }
}
