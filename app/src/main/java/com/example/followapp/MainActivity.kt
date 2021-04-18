package com.example.followapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Follow App"

        //Crezione bottone e inizializzazione PlusB
        val PlusButton = findViewById<Button>(R.id.plusB)

        val listaEsami = findViewById<Button>(R.id.tVDatiEsami)


        //Evento click PlusB in cui viene aperta l'activity ExamInsertion
        PlusButton.setOnClickListener {
            val intent = Intent(this, ExamInsertion::class.java)
            startActivity(intent)
        }
    }

    /**
     * Funzione per leggere la lista dei valori presenti nel DB.
     */
    private fun getItemsList(): ArrayList<modelExam> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val examList: ArrayList<modelExam> = databaseHandler.vistaEsami()

        return examList
    }


    /**
     * Funzione per mostrare la lista degli esami inseriti.
     */


}