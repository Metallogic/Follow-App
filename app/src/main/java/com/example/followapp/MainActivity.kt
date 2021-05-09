package com.example.followapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

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

        //Crezione bottone e inizializzazione listaE
        val listaEsamiVecchi = findViewById<Button>(R.id.listaE)
        //Evento click listaE in cui viene aperta l'activity ExamInsertion
        listaEsamiVecchi.setOnClickListener {
            val intent = Intent(this, lista_esami_passati::class.java)
            startActivity(intent)
        }

    }

    /**
     * Funzione che nella fase onResume aggiorna/controlla la lista degli esami inseriti
     */
    override fun onResume() {
        super.onResume()
        viewExam()
    }

    /**
     * Funzione usata per mostrare la lista dei dati nella UI con data > di quella attuale.
     */
    private fun viewExam() {
        //Lettura data attuale
        val dataAttuale = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MM/yyyy")
        val dataAttualeFormattata = df.format(dataAttuale)

        var listaEsami = dbHandler.vistaEsami(this, dataAttualeFormattata)
        val adattatore = examAdapter(listaEsami, this)
        var rVlistaEsami = findViewById<RecyclerView>(R.id.rVDatiEsami)

        rVlistaEsami.layoutManager = LinearLayoutManager(this)
        rVlistaEsami.adapter = adattatore
    }



}