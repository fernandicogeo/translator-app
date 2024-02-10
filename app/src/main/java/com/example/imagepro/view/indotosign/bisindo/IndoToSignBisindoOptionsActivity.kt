package com.example.imagepro.view.indotosign.bisindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.imagepro.R

class IndoToSignBisindoOptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indo_to_sign_bisindo_options)

        val text = findViewById<CardView>(R.id.card_text_to_sign)
        val voice = findViewById<CardView>(R.id.card_voice_to_sign)

        text.setOnClickListener {
            openBisindoTextToSignActivity()
        }

        voice.setOnClickListener {
            openBisindoVoiceToSignActivity()
        }
    }

    private fun openBisindoTextToSignActivity() {
        val intent = Intent(this, BisindoTextToSignActivity::class.java)
        startActivity(intent)
    }

    private fun openBisindoVoiceToSignActivity() {
        val intent = Intent(this, BisindoVoiceToSignActivity::class.java)
        startActivity(intent)
    }
}