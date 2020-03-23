package com.example.mechanic.dialogBox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.mechanic.R;

public class RequestSentDialogBox extends Dialog {

    public RequestSentDialogBox(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_sent_dialog_box);
    }
}
