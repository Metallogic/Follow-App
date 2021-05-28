package com.example.followapp

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.*


class ModificaEsamiOld : AppCompatActivity() {

    companion object {
        val ID_ESAME = "COLUMN_ID"
        val NOME_ESAME = "COLUMN_NAME_NOMESAME"
        val DATA_ESAME = "COLUMN_NAME_DATA"
        val ORA_ESAME = "COLUMN_NAME_ORA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifica_esami_old)

        val nomeE = findViewById<TextView>(R.id.eTNomeEsameM)
        val dataE = findViewById<TextView>(R.id.tVDataM)
        val oraE = findViewById<TextView>(R.id.tVOraM)

        /**
         * Settaggio dati nell'activity
         */
        //Get variabili passati dall'intent
        val intent = intent
        val idEsame = intent.getIntExtra(ID_ESAME, -1)
        val nomeEsame = intent.getStringExtra(NOME_ESAME)
        val dataEsame = intent.getStringExtra(DATA_ESAME)
        val oraEsame = intent.getStringExtra(ORA_ESAME)

        //Settaggio valori nellle EditText e TextView
        nomeE.setText(nomeEsame)
        dataE.setText(dataEsame)
        oraE.setText(oraEsame)

        /**
         * -- GESTIONE ANNULLAMENTO OPERAZIONE --
         */
        //Crezione bottone e inizializzazione annullaB
        val annullaOperazione = findViewById<Button>(R.id.annullaB)
        //Evento click annullaB in cui viene annulla l'operazione di modifica esame e ritorno al main activity
        annullaOperazione.setOnClickListener {
            Toast.makeText(applicationContext, R.string.operazione_annullata, Toast.LENGTH_SHORT)
                .show()
            //Chiusura activity
            finish()
        }

        /**
         * -- GESTIONE ELIMINAZIONE ESAME --
         */
        //Avviso di sicurezza
        val avvisoD = AlertDialog.Builder(this)
        avvisoD.setTitle(getString(R.string.attenzione_msg))
        avvisoD.setMessage(getString(R.string.sicuro_eliminare_msg))
        avvisoD.setPositiveButton(getString(R.string.SI)) { _, _ ->
            //Chiamata funzione per eliminare l'esame e aggiornare lo stato del DB
            eliminaRiga(idEsame)
            finish()
        }
        avvisoD.setNegativeButton(getString(R.string.NO)) { _, _ ->
            //linea vuota, la finiestra di dialogo si chiude
        }
        //Crezione bottone e inizializzazione deleteB
        val deleteB = findViewById<Button>(R.id.deleteBtrash)
        //Evento click deleteB in cui vengono eliminati i dati dell'esame
        deleteB.setOnClickListener {
            avvisoD.show()
        }
    }

    /**
     * Funzione che permette l'eliminazione della riga relativa all'esame in oggetto
     */
    fun eliminaRiga(idEsame: Int) {
        val databaseHandler = DatabaseHandler(this)
        val status = databaseHandler.trashExam(idEsame)
        if (status > -1) {
            Toast.makeText(applicationContext, R.string.esame_cestinato_toast, Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(applicationContext, R.string.errore_esame, Toast.LENGTH_LONG).show()
        }
    }

}
