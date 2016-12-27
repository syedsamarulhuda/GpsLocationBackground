package com.example.samar.runappbackgorund;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BackgroundAppExample extends AppCompatActivity {

    public static boolean isService = false;

    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionFine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionFine != PackageManager.PERMISSION_GRANTED && permissionCoarse != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    BackgroundAppExample.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


        Button startserviceButton = (Button) findViewById(R.id.button1);
        startserviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(BackgroundAppExample.this,BackgroundService.class));
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                isService = true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(BackgroundAppExample.this,
                BackgroundService.class));
        if(isService)
        {
            TextView tv = (TextView) findViewById(R.id.textView1);
            tv.setText("Service Resumed");
            isService = false;
        }
    }

}
