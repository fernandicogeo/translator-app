package com.example.imagepro.view.signtoindo.bisindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imagepro.R;

public class SignToIndoBisindoOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_to_indo_bisindo_options);

        CardView text = findViewById(R.id.card_sign_to_text);
        CardView voice = findViewById(R.id.card_sign_to_voice);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBisindoSignToTextActivity();
            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBisindoSignToVoiceActivity();
            }
        });
    }

    private void openBisindoSignToTextActivity() {
        Intent intent = new Intent(this, BisindoSignToTextActivity.class);
        startActivity(intent);
    }

    private void openBisindoSignToVoiceActivity() {
        Intent intent = new Intent(this, BisindoSignToVoiceActivity.class);
        startActivity(intent);
    }
}