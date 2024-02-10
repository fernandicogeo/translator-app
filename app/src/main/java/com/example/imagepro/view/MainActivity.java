package com.example.imagepro.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.imagepro.R;
import com.example.imagepro.view.indotosign.IndoToSignOptionsActivity;
import com.example.imagepro.view.signtoindo.SignToIndoOptionsActivity;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signToIndoButton = findViewById(R.id.signToIndo);
        Button indoToSignButton = findViewById(R.id.indoToSign);

        signToIndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermissionAndOpenActivity();
            }
        });

        indoToSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIndoToSignOptionsActivity();
            }
        });
    }

    private void checkCameraPermissionAndOpenActivity() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission if not granted
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST
            );
        } else {
            // If permission granted, proceed to open SignToIndoOptionsActivity
            openSignToIndoOptionsActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted by user
                openSignToIndoOptionsActivity();
            } else {
                // Camera permission denied by user
                // You can handle this according to your app's requirements
            }
        }
    }

    private void openSignToIndoOptionsActivity() {
        Intent intent = new Intent(this, SignToIndoOptionsActivity.class);
        startActivity(intent);
    }

    private void openIndoToSignOptionsActivity() {
        Intent intent = new Intent(this, IndoToSignOptionsActivity.class);
        startActivity(intent);
    }
}