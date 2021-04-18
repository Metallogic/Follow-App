package com.example.followapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


class ExamInsertion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_insertion)


        /**
         * -- GESTIONE NOME ESAME --
         */

        var eTNomeEsame = findViewById<TextView>(R.id.eTNomeEsame)


        /**
         * -- GESTIONE CALENDARIO --
         */

        //Crezione val per textView e inizializzazione per inserimento data
        var tVDaTa = findViewById<TextView>(R.id.tVData)

        //Calendario scelta data
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        //Crezione bottone e inizializzazione per inserimento data
        val dataButton = findViewById<Button>(R.id.dataB)
        //Evento click dataB in cui viene aperto il calendario
        dataButton.setOnClickListener {
            val dataCalendario = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                tVDaTa.setText("" + mDayOfMonth + "/" + (mMonth+1) + "/" + mYear)
            }, year, month, day)

            dataCalendario.show()
        }


        /**
         * -- GESTIONE OROLOGIO --
         */

        //Orologio scelta ora
        val orologio = Calendar.getInstance()

        //Crezione val per textView e inizializzazione per inserimento ora
        val tVOrA = findViewById<TextView>(R.id.tVOra)

        //Crezione bottone e inizializzazione per inserimento ora
        var oraButton = findViewById<Button>(R.id.oraB)

        //Evento click oraB in cui viene aperto l'orologio
        oraButton.setOnClickListener {
            val oraCalendario = TimePickerDialog.OnTimeSetListener { TimePicker, mHour, mMinute ->
                orologio.set(Calendar.HOUR_OF_DAY, mHour)
                orologio.set(Calendar.MINUTE, mMinute)

                tVOrA.text = SimpleDateFormat("HH:mm").format(orologio.time).toString()
            }

            //Visualizzazione ora selezionata
            TimePickerDialog(this, oraCalendario, orologio.get(Calendar.HOUR_OF_DAY), orologio.get(Calendar.MINUTE), true).show()

        }

        /**
         * -- GESTIONE SALVATAGGIO --
         */

        //Crezione bottone e inizializzazione bottone salvaB
        var salvataggioButton = findViewById<Button>(R.id.salvaB)

        // Creazione messaggio allerta se dati non inseriti
        //Evento click salvaB in cui vengono salvati i dati inserite dall'utente
        salvataggioButton.setOnClickListener {
            //Controllo dati inseriti, se incompleti messagio di errore, altrimenti toast di salvataggio andato a buon fine
            if (TextUtils.isEmpty(tVDaTa.getText().toString()) || TextUtils.isEmpty(tVOrA.getText().toString()) || TextUtils.isEmpty(eTNomeEsame.getText().toString())) {
                val messaggioAllerta = AlertDialog.Builder(this@ExamInsertion)
                messaggioAllerta.setTitle("ATTENZIONE")
                if (TextUtils.isEmpty(tVDaTa.getText().toString()) and !TextUtils.isEmpty(tVOrA.getText().toString()) and !TextUtils.isEmpty(eTNomeEsame.getText().toString())) {
                    messaggioAllerta.setMessage("Dati inseriti incompleti!")
                    messaggioAllerta.setMessage("Inserire data esame")
                    messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                        dialog.dismiss()
                    }
                }
                if (TextUtils.isEmpty(tVDaTa.getText().toString()) and TextUtils.isEmpty(tVOrA.getText().toString()) and !TextUtils.isEmpty(eTNomeEsame.getText().toString())) {
                    messaggioAllerta.setMessage("Dati inseriti incompleti!")
                    messaggioAllerta.setMessage("Inserire data e ora esame")
                    messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                        dialog.dismiss()
                    }
                }
                if (TextUtils.isEmpty(tVDaTa.getText().toString()) and TextUtils.isEmpty(tVOrA.getText().toString()) and TextUtils.isEmpty(eTNomeEsame.getText().toString())) {
                    messaggioAllerta.setMessage("Dati inseriti incompleti!")
                    messaggioAllerta.setMessage("Inserire nome, data e ora esame")
                    messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                        dialog.dismiss()
                    }
                }
                if (!TextUtils.isEmpty(tVDaTa.getText().toString()) and !TextUtils.isEmpty(tVOrA.getText().toString()) and TextUtils.isEmpty(eTNomeEsame.getText().toString())) {
                    messaggioAllerta.setMessage("Dati inseriti incompleti!")
                    messaggioAllerta.setMessage("Inserire nome esame")
                    messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                        dialog.dismiss()
                    }
                }
                if (TextUtils.isEmpty(tVOrA.getText().toString()) and !TextUtils.isEmpty(tVDaTa.getText().toString()) and !TextUtils.isEmpty(eTNomeEsame.getText().toString())) {
                    messaggioAllerta.setMessage("Dati inseriti incompleti!")
                    messaggioAllerta.setMessage("Inserire ora esame")
                    messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                        dialog.dismiss()
                    }
                }
                if (!TextUtils.isEmpty(tVDaTa.getText().toString()) and TextUtils.isEmpty(tVOrA.getText().toString()) and TextUtils.isEmpty(eTNomeEsame.getText().toString())) {
                    messaggioAllerta.setMessage("Dati inseriti incompleti!")
                    messaggioAllerta.setMessage("Inserire nome e ora esame")
                    messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                        dialog.dismiss()
                    }
                }

                if (TextUtils.isEmpty(tVDaTa.getText().toString()) and !TextUtils.isEmpty(tVOrA.getText().toString()) and TextUtils.isEmpty(eTNomeEsame.getText().toString())) {
                    messaggioAllerta.setMessage("Dati inseriti incompleti!")
                    messaggioAllerta.setMessage("Inserire nome e data esame")
                    messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                        dialog.dismiss()
                    }
                }
                messaggioAllerta.show()
            }
            //Inserimento dati nel DB e Toast di avvenuto inserimento esame
            else {
                aggiungiRecord()
                //Chiusura activity
                finish()

            }
        }
    }


    /**
     * Funzione per salvare i record degli esami nel DB.
     */

    private fun aggiungiRecord() {
        var eTNomeEsame = findViewById<TextView>(R.id.eTNomeEsame)
        val nomeE = eTNomeEsame.text.toString()

        var tVDaTa = findViewById<TextView>(R.id.tVData)
        val dataE = tVDaTa.text.toString()

        var tVOra = findViewById<TextView>(R.id.tVOra)
        val oraE = tVOra.text.toString()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.addExam(modelExam(nomeE, dataE,oraE))
        if (status > -1) {
            Toast.makeText(applicationContext, R.string.esame_inserito_toast, Toast.LENGTH_LONG).show()
        }
    }

}

