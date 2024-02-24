package com.example.imagepro.view.signtoindo.sibi;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.imagepro.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.Locale;

public class SibiSignToVoiceActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "SibiSignToVoiceActivity";
    Button btnConvert;
    String result="nothing";
    TextToSpeech textToSpeech;
    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase mOpenCvCameraView;
    private objectDetectorSibiVoiceClass objectDetectorSibiVoiceClass;
    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface
                    .SUCCESS) {
                Log.i(TAG, "OpenCv Is loaded");
                mOpenCvCameraView.enableView();
            }
            super.onManagerConnected(status);
        }
    };

    public SibiSignToVoiceActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int MY_PERMISSIONS_REQUEST_CAMERA = 0;
        if (ContextCompat.checkSelfPermission(SibiSignToVoiceActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SibiSignToVoiceActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

        setContentView(R.layout.activity_sibi_sign_to_voice);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.frame_Surface);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        try {
            objectDetectorSibiVoiceClass = new objectDetectorSibiVoiceClass(getAssets(), "hand_model.tflite", "custom_label.txt", 300, "signtotextsibi.tflite", 96);
            Log.d("SibiSignToVoiceActivity", "Model is successfully loaded");
        } catch (IOException e) {
            Log.d("SibiSignToVoiceActivity", "Getting some error");
            e.printStackTrace();
        }

        btnConvert = findViewById(R.id.btnConvert);

        // create an object textToSpeech and adding features into it
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        // Adding OnClickListener
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(result,TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "Opencv initialization is done");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.d(TAG, "Opencv is not loaded. try again");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
    }

    public void onCameraViewStopped() {
        mRgba.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        Mat out = new Mat();
        out = objectDetectorSibiVoiceClass.recognizeImage(mRgba);
        result = objectDetectorSibiVoiceClass.stringImage(mRgba);
        return out;
    }
}