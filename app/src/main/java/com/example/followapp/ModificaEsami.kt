package com.example.followapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class ModificaEsami : AppCompatActivity() {

    companion object {
        val ID_ESAME = "COLUMN_ID"
        val NOME_ESAME = "COLUMN_NAME_NOMESAME"
        val DATA_ESAME = "COLUMN_NAME_DATA"
        val ORA_ESAME = "COLUMN_NAME_ORA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifica_esami)

        var nomeE = findViewById<EditText>(R.id.eTNomeEsameM)
        var dataE = findViewById<TextView>(R.id.tVDataM)
        var oraE = findViewById<TextView>(R.id.tVOraM)

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

        /*val messaggioAllerta = AlertDialog.Builder(this)
            messaggioAllerta.setTitle("ATTENZIONE")
            messaggioAllerta.setMessage("Siuro di voler modificare i dati dell'esame?")
            messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                dialog.dismiss()*/


        //Crezione bottone e inizializzazione annullaB
        val annullaOperazione = findViewById<Button>(R.id.annullaB)
        //Evento click annullaB in cui viene annulla l'operazione di modifica esame e ritorno al main activity
        annullaOperazione.setOnClickListener {
            Toast.makeText(applicationContext, R.string.operazione_annullata, Toast.LENGTH_SHORT).show()
            //Chiusura activity
            finish()
        }

        //Crezione bottone e inizializzazione salvaB
        val saveChangeB = findViewById<Button>(R.id.salvaB)
        //Evento click salvaB in cui vengono modificati i dati dell'esame
        saveChangeB.setOnClickListener {
            //Chiamata funzione per modificare l'esame e aggiornare lo stato del DB
            modificaRiga(idEsame)
            //Chiusura activity
            finish()
        }

        //Crezione bottone e inizializzazione deleteB
        val deleteB = findViewById<Button>(R.id.deleteB)
        //Evento click deleteB in cui vengono eliminati i dati dell'esame
        deleteB.setOnClickListener {
            //Chiamata funzione per eliminare l'esame e aggiornare lo stato del DB
            eliminaRiga(idEsame)
            //Chiusura activity
            finish()
        }
    }

    /**
     * Funzione che permette la modifica della riga relativa all'esame in oggetto
     */
    fun modificaRiga(idEsame: Int) {
        //Inizializzazione campi da modificare
        var eTNomeEsame = findViewById<EditText>(R.id.eTNomeEsameM)
        val nomeE = eTNomeEsame.text.toString()

        var tVDaTa = findViewById<TextView>(R.id.tVDataM)
        val dataE = tVDaTa.text.toString()

        var tVOra = findViewById<TextView>(R.id.tVOraM)
        val oraE = tVOra.text.toString()

        //Aggiornamento DB con nuovi valori
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.updateExam(idEsame, nomeE, dataE, oraE)
        if (status > -1) {
            Toast.makeText(applicationContext, R.string.esame_modificato_toast, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, R.string.errore_esame, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Funzione che permette l'eliminazione della riga relativa all'esame in oggetto
     */
    fun eliminaRiga(idEsame: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.deleteExam(idEsame)
        if (status > -1) {
            Toast.makeText(applicationContext, R.string.esame_eliminato_toast, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, R.string.errore_esame, Toast.LENGTH_LONG).show()
        }
    }
}