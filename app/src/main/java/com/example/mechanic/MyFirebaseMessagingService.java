package com.example.mechanic;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        Log.d("ClassFirebaseMessaging", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);


        FirebaseMessaging.getInstance().subscribeToTopic("mechanic")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (!task.isSuccessful()) {
                                Log.d("firebaseInstance","Can't register to mechanic");
                                //Toast.makeText(LoginActivity.this, "Can't register to mechanic", Toast.LENGTH_SHORT).show();
                            }

                            Log.d("firebaseInstance", "Registered to mechanic");
                            //Toast.makeText(LoginActivity.this, "Registered to mechanic", Toast.LENGTH_SHORT).show();
                        }
                    });
    }
}
