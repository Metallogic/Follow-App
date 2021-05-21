package com.example.followapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class cestino : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cestino)
        viewExam()
    }

    /**
     * Funzione che nella fase onResume aggiorna/controlla la lista degli cestinati
     */
    override fun onResume() {
        super.onResume()
        viewExam()
    }

    /**
     * Funzione usata per mostrare la lista degli esami cestinati
     */
    private fun viewExam() {
        val listaEsami = MainActivity.dbHandler.vistaEsamiCestinati(this)
        val adattatore = trashAdapter(listaEsami, this)
        val rVlistaEsami = findViewById<RecyclerView>(R.id.rVesami)

        rVlistaEsami.layoutManager = LinearLayoutManager(this)
        rVlistaEsami.adapter = adattatore
    }
}
