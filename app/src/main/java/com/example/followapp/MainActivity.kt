package com.example.followapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var dbHandler: DatabaseHandler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Follow App"

        dbHandler = DatabaseHandler(this)
        viewExam()

        //Crezione bottone e inizializzazione PlusB
        val PlusButton = findViewById<Button>(R.id.plusB)
        //Evento click PlusB in cui viene aperta l'activity ExamInsertion
        PlusButton.setOnClickListener {
            val intent = Intent(this, ExamInsertion::class.java)
            startActivity(intent)
        }

        //Crezione bottone e inizializzazione refreshB
        val RefreshButton = findViewById<Button>(R.id.refreshB)
        //Evento click refreshB in cui si aggiorna la lista degli esami
        RefreshButton.setOnClickListener {
            viewExam()
        }


    }

    /**
     * Funzione usata per mostrare la lista dei dati nella UI.
     */
    private fun viewExam() {

        var listaEsami = dbHandler.vistaEsami(this)
        val adattatore = examAdapter(listaEsami)
        var rVlistaEsami = findViewById<RecyclerView>(R.id.rVDatiEsami)

        rVlistaEsami.layoutManager = LinearLayoutManager(this)
        rVlistaEsami.adapter = adattatore

    }

}