package com.example.mechanic;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class BarCodeLoginActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    boolean isDetected = false;
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private Button scanBtn;
    ImageButton flashlight1 ;
    int flash_ON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_login);

        scannerView = findViewById(R.id.ScannerFrontend);
        flashlight1 = findViewById(R.id.flashlight1);
        flash_ON = 0;


    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(BarCodeLoginActivity.this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA},REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case REQUEST_CAMERA:
                if(grantResults.length>0) {
                    boolean CameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (CameraAccepted)
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage("You need to allow for both permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                            }
                                        });
                                return ;
                            }
                        }
                    }
                }
                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                if(scannerView==null)
                {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(BarCodeLoginActivity.this);
                scannerView.startCamera();
            }
            else
            {
                requestPermission();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }


    public void displayAlertMessage(String message, DialogInterface.OnClickListener Listener)
    {

    }

    @Override
    public void handleResult(Result result) {
        String scanResult = result.getText();
        Log.i("sudhanshu",scanResult);
        Intent data = new Intent();
        data.putExtra("Scan_result", scanResult);
        setResult(RESULT_OK, data);
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);;
        finish();
    }


    public void FlashLight(View v)
    {
        if(flash_ON==0)
        {
            flash_ON =1;
            flashlight1.setImageResource(R.drawable.ic_flash_off_black_24dp);
            scannerView.setFlash(true);
            //Make flash on
        }
        else
        {
            flash_ON = 0;
            flashlight1.setImageResource(R.drawable.ic_flash_on_black_24dp);
            scannerView.setFlash(false);
        }
    }

}
