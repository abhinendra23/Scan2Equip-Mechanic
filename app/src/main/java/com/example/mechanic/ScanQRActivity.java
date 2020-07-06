package com.example.mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScanQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    boolean isDetected = false;
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private Button scanBtn;
    ImageButton flashlight1 ;
    int flash_ON;

    FirebaseVisionBarcodeDetector detector;
    FirebaseVisionBarcodeDetectorOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        scannerView = findViewById(R.id.ScannerFrontend);
        flashlight1 = findViewById(R.id.flashlight1);
        flash_ON = 0;

//
//        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//        {
//            requestPermissions(new String[]{Manifest.permission.CAMERA},1);
//        }
//        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
//        {
//            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},1);
//        }
//        Dexter.withActivity(this)
//                .withPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
//                .withListener(new MultiplePermissionsListener() {
//
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//
//                        if(scannerView==null)
//                        {
//                            scannerView = new ZXingScannerView(ScanQRActivity.this);
//                            setContentView(scannerView);
//                        }
//                        scannerView.setResultHandler(this);
//                        scannerView.startCamera();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//
//                    }
//                }).check();
//
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
//        }


    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(ScanQRActivity.this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermission()
    {
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
                scannerView.setResultHandler(ScanQRActivity.this);
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

        Intent i = new Intent(ScanQRActivity.this, GetMachineDetailsActivity.class);
        i.putExtra("generationCode",scanResult);
        startActivity(i);
    }
//
//    private void setupCamera()
//    {
//        cameraView.addFrameProcessor(new FrameProcessor() {
//            @Override
//            public void process(@NonNull Frame frame) {
//                processImage(getVisionImageFromFrame(frame));
//            }
//        });
//
//        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
//                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE).build();
//
//        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
//    }
//
//    private void processImage(FirebaseVisionImage image) {
//        if(!isDetected)
//        {
//            detector.detectInImage(image)
//                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
//                        @Override
//                        public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
//                            processResult(firebaseVisionBarcodes);
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ScanQRActivity.this,""+ e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }
//
//    private FirebaseVisionImage getVisionImageFromFrame(Frame frame)
//    {
//        byte[] data = frame.getData();
//        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
//                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
//                .setWidth(frame.getSize().getWidth())
//                .setHeight(frame.getSize().getHeight())
//                .build();
//
//        return FirebaseVisionImage.fromByteArray(data, metadata);
//    }
//
//    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes)
//    {
//        if(firebaseVisionBarcodes.size()>0 && isDetected==false)
//        {
//            for(FirebaseVisionBarcode item : firebaseVisionBarcodes)
//            {
//
//                isDetected = true;
//                finish();
//                // createDialog(item.getRawValue());
//            }
//        }
//    }


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
