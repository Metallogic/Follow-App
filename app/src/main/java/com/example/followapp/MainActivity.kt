package com.example.followapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    }

    /**
     * Funzione di creazione del menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu);
        return true
    }

    /**
     * Funzione che gestisce il click degli item del menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;
        if (id == R.id.menuNuovoEsame){
            val intent = Intent(this, ExamInsertion::class.java)
            startActivity(intent)
            return true
        }else if (id == R.id.menuArchivio){
            val intent = Intent(this, Old_Exam::class.java)
            startActivity(intent)
            return true
        }else if (id == R.id.menuCestino) {
            val intent = Intent(this, cestino::class.java)
            startActivity(intent)
            return true
        }
       return super.onOptionsItemSelected(item)
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
        val df = SimpleDateFormat("yyyy-MM-dd")
        val dataAttualeFormattata = df.format(dataAttuale)

        //aggiornamento date nel DB con data attuale tramite funzione updateDate()
        dbHandler.updateDate()
        var listaEsami = dbHandler.vistaEsami(this, dataAttualeFormattata)
        val adattatore = examAdapter(listaEsami, this)
        var rVlistaEsami = findViewById<RecyclerView>(R.id.rVesami)

        rVlistaEsami.layoutManager = LinearLayoutManager(this)
        rVlistaEsami.adapter = adattatore
    }

}