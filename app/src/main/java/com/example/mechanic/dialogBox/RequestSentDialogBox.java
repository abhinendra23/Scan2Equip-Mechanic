package com.example.mechanic.dialogBox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.mechanic.R;

public class RequestSentDialogBox extends Dialog implements View.OnClickListener{

    ImageView cancel;
    Button okayButton;
    Button update;
    Activity a;

    public RequestSentDialogBox(Activity a) {
        super(a);
        this.a = a;
        update = a.findViewById(R.id.submit_update);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.request_sent_dialog_box);
        okayButton = findViewById(R.id.okay_button);
        cancel = findViewById(R.id.cancel);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setText("Request Updated");
                update.setEnabled(false);
                dismiss();
                a.finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setText("Request Updated");
                update.setEnabled(false);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {


    }
}
