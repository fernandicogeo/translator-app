package com.example.imagepro.view.indotosign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.imagepro.R
import com.example.imagepro.view.indotosign.bisindo.IndoToSignBisindoOptionsActivity
import com.example.imagepro.view.indotosign.sibi.IndoToSignSibiOptionsActivity

class IndoToSignOptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indo_to_sign_options)

        val bisindo = findViewById<CardView>(R.id.card_bisindo)
        val sibi = findViewById<CardView>(R.id.card_sibi)

        bisindo.setOnClickListener {
            openIndoToSignBisindoOptionsActivity()
        }

        sibi.setOnClickListener {
            openIndoToSignSibiOptionsActivity()
        }
    }

    private fun openIndoToSignBisindoOptionsActivity() {
        val intent = Intent(this, IndoToSignBisindoOptionsActivity::class.java)
        startActivity(intent)
    }

    private fun openIndoToSignSibiOptionsActivity() {
        val intent = Intent(this, IndoToSignSibiOptionsActivity::class.java)
        startActivity(intent)
    }
}