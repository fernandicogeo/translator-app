package com.example.imagepro.view.indotosign.sibi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.imagepro.R
import com.example.imagepro.view.indotosign.sibi.SibiTextToSignActivity
import com.example.imagepro.view.indotosign.sibi.SibiVoiceToSignActivity

class IndoToSignSibiOptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indo_to_sign_sibi_options)

        val text = findViewById<CardView>(R.id.card_text_to_sign)
        val voice = findViewById<CardView>(R.id.card_voice_to_sign)

        text.setOnClickListener {
            openSibiTextToSignActivity()
        }

        voice.setOnClickListener {
            openSibiVoiceToSignActivity()
        }
    }

    private fun openSibiTextToSignActivity() {
        val intent = Intent(this, SibiTextToSignActivity::class.java)
        startActivity(intent)
    }

    private fun openSibiVoiceToSignActivity() {
        val intent = Intent(this, SibiVoiceToSignActivity::class.java)
        startActivity(intent)
    }
}