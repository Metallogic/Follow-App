package com.example.followapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Follow App"

        //Crezione bottone e inizializzazione PlusB
        val PlusButton = findViewById<Button>(R.id.plusB)


        //Evento click PlusB in cui viene aperta l'activity ExamInsertion
        PlusButton.setOnClickListener {
            val intent = Intent(this, ExamInsertion::class.java)
            startActivity(intent)
        }
    }


}