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



        /*
        //Crezione bottone e inizializzazione modifyB
        val moficicaEsame = findViewById<ImageButton>(R.id.modifyB)
        //Evento click modifyB in cui apro l'activity per modificare l'esame
        moficicaEsame.setOnClickListener {
            val intent = Intent(this, ModificaEsami::class.java)
            startActivity(intent)
        }
        */

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
/*
    /**
    * Funzione usata per modificare gli esami inseriti.
    */
    fun updateRecord(modelloEsame: modelExam) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        val eTnomeE = findViewById<EditText>(R.id.eTNomeEsameM)
        val tVdatE = findViewById<TextView>(R.id.tVDataM)
        val tVoraE = findViewById<TextView>(R.id.tVOraM)


        updateDialog.set
        etContentView(R.layout.activity_modifica_esami)

        updateDialog.etUpdateEmailId.setText(modelloEsame.nomeEsame)
        updateDialog.eTnomeE.setText(modelloEsame.dataEsame)
        updateDialog.eTnomeE.setText(modelloEsame.oraEsame)


        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener {

            val name = updateDialog.etUpdateName.text.toString()
            val email = updateDialog.etUpdateEmailId.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (!name.isEmpty() && !email.isEmpty()) {
                val status =
                    databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, email))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        updateDialog.show()
    }
    */
}