package com.example.followapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class lista_esami_passati : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_esami_passati)
        viewOldExam()
    }

    /**
     * Funzione che nella fase onResume aggiorna/controlla la lista degli esami vecchi
     */
    override fun onResume() {
        super.onResume()
        viewOldExam()
    }

    /**
     * Funzione usata per mostrare la lista dei dati nella UI con data > di quella attuale.
     */
    private fun viewOldExam() {
        //Lettura data attuale
        val dataAttuale = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MM/yyyy")
        val dataAttualeFormattata = df.format(dataAttuale)

        var listaEsami = MainActivity.dbHandler.vistaEsamiVecchi(this, dataAttualeFormattata)
        val adattatore = oldExamAdapter(listaEsami, this)
        var rVlistaEsami = findViewById<RecyclerView>(R.id.rVDatiEsami)

        rVlistaEsami.layoutManager = LinearLayoutManager(this)
        rVlistaEsami.adapter = adattatore
    }
}