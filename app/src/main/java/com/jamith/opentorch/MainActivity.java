package com.jamith.opentorch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewStatus;
    private ImageButton imgBtnSwitch;
    private static final  int camera_request=50;
    private boolean hasACameraFlash;
    private boolean flashlightStatus=false;
    private boolean isEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewStatus = findViewById(R.id.imageViewStatus);
        imgBtnSwitch = findViewById(R.id.imgBtnSwitch);

        hasACameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        isEnable = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED;

        imgBtnSwitch.setEnabled(!isEnable);
        imageViewStatus.setEnabled(isEnable);
    }
    public void imgBtnSwitchOnClicked(View view){
        ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.CAMERA},camera_request);
        imgBtnSwitch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                  if (hasACameraFlash){
                      if (flashlightStatus){
                          setFlashLightOff();
                      }else {
                          setFlashLightOn();
                      }
                  }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setFlashLightOn(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String camID = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(camID,true);
            flashlightStatus=true;
            imageViewStatus.setImageResource(R.drawable.torch_on);
            imgBtnSwitch.setImageResource(R.drawable.icon_off);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setFlashLightOff(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String camID = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(camID,false);
            flashlightStatus=false;
            imageViewStatus.setImageResource(R.drawable.torch);
            imgBtnSwitch.setImageResource(R.drawable.icon_on);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case camera_request:
            if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                imgBtnSwitch.setEnabled(false);
                imageViewStatus.setEnabled(true);

            }else {
                Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
}
