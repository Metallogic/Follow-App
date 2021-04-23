package com.example.followapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ModificaEsami : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifica_esami)

        var nomeE = findViewById<EditText>(R.id.eTNomeEsameM)
        var dataE = findViewById<TextView>(R.id.tVDataM)
        var oraE = findViewById<TextView>(R.id.tVOraM)

        /**
         * Settaggio dati nell'activity
         */
        //Variabili passati dall'intent
        val idEsame = intent.getIntExtra(examAdapter.ID_ESAME, -1)
        val nomeEsame = intent.getStringExtra(examAdapter.NOME_ESAME).toString()
        val dataEsame = intent.getStringArrayExtra(examAdapter.DATA_ESAME).toString()
        val oraEsame = intent.getStringArrayExtra(examAdapter.ORA_ESAME).toString()

        nomeE.setText(nomeEsame)
        dataE.setText(dataEsame)
        oraE.setText(oraEsame)
        
        //Crezione bottone e inizializzazione salvaB
        val saveChangeB = findViewById<Button>(R.id.salvaB)
        //Evento click salvaB in cui vengono modificati i dati dell'esame
        saveChangeB.setOnClickListener {

        }

        //Crezione bottone e inizializzazione deleteB
        val deleteB = findViewById<Button>(R.id.deleteB)
        //Evento click deleteB in cui vengono eliminati i dati dell'esame
        saveChangeB.setOnClickListener {

        }
    }

    fun clearData(){

    }
}