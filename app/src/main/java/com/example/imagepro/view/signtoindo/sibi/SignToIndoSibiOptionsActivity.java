package com.example.imagepro.view.signtoindo.sibi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imagepro.R;

public class SignToIndoSibiOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_to_indo_sibi_options);

        CardView text = findViewById(R.id.card_sign_to_text);
        CardView voice = findViewById(R.id.card_sign_to_voice);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSibiSignToTextActivity();
            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSibiSignToVoiceActivity();
            }
        });
    }

    private void openSibiSignToTextActivity() {
        Intent intent = new Intent(this, SibiSignToTextActivity.class);
        startActivity(intent);
    }

    private void openSibiSignToVoiceActivity() {
        Intent intent = new Intent(this, SibiSignToVoiceActivity.class);
        startActivity(intent);
    }
}