package com.example.followapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

/**
 * Classe che mostra logo caricamento app al'avvio dell'app
 */
class SchermataCaricamentoApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schermata_caricamento_app)

        //Inizializzazione logo ImageView
        val logoImg = findViewById<ImageView>(R.id.logoImg)

        //Inizializzazione animazione logo ad avvio app
        //durata animazione = 1500 ms
        logoImg.animate().setDuration(1500).alpha(1f).withEndAction {
            //inizializzazione intent che crea la main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}