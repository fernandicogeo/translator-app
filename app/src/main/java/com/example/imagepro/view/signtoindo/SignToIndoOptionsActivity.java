package com.example.imagepro.view.signtoindo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imagepro.R;
import com.example.imagepro.view.signtoindo.bisindo.SignToIndoBisindoOptionsActivity;
import com.example.imagepro.view.signtoindo.sibi.SignToIndoSibiOptionsActivity;

public class SignToIndoOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_to_indo_options);

        CardView bisindo = findViewById(R.id.card_bisindo);
        CardView sibi = findViewById(R.id.card_sibi);

        bisindo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignToIndoBisindoOptionsActivity();
            }
        });

        sibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignToIndoSibiOptionsActivity();
            }
        });
    }

    private void openSignToIndoBisindoOptionsActivity() {
        Intent intent = new Intent(this, SignToIndoBisindoOptionsActivity.class);
        startActivity(intent);
    }

    private void openSignToIndoSibiOptionsActivity() {
        Intent intent = new Intent(this, SignToIndoSibiOptionsActivity.class);
        startActivity(intent);
    }
}