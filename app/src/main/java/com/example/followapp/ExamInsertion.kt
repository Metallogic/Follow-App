package com.example.followapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
        var eTNomeEsame = findViewById<TextView>(R.id.eTNomeEsameM)


        /**
         * -- GESTIONE CALENDARIO --
         */
        //Crezione val per textView e inizializzazione per inserimento data
        var tVDaTa = findViewById<TextView>(R.id.tVDataM)
        //Calendario scelta data
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)
        //Crezione bottone e inizializzazione per inserimento data
        val dataButton = findViewById<Button>(R.id.dataB)
        //Evento click dataB in cui viene aperto il calendario
        dataButton.setOnClickListener {
            //do{
                               val dataCalendario = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                    tVDaTa.setText("" + mDayOfMonth + "/" + (mMonth+1) + "/" + mYear)
                }, year, month, day)
                dataCalendario.show()
           // } while (data passata? e si allora reinserire data esame)
        }


        /**
         * -- GESTIONE OROLOGIO --
         */
        //Orologio scelta ora
        val orologio = Calendar.getInstance()
        //Crezione val per textView e inizializzazione per inserimento ora
        val tVOrA = findViewById<TextView>(R.id.tVOraM)
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
        //Evento click salvaB in cui vengono salvati i dati inserite dall'utente
        salvataggioButton.setOnClickListener {
            // Creazione messaggio allerta se dati non inseriti
            val messaggioAllerta = AlertDialog.Builder(this@ExamInsertion)
            messaggioAllerta.setTitle("ATTENZIONE")
            messaggioAllerta.setMessage("Dati inseriti incompleti!")
            messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                dialog.dismiss()
            }
            //Controllo dati inseriti, se incompleti messagio di errore, altrimenti toast di salvataggio andato a buon fine
            val msg= checkDati(eTNomeEsame.getText().toString(),tVDaTa.getText().toString(),tVOrA.getText().toString(),)
            //Inserimento dati nel DB e Toast di avvenuto inserimento esame
            if(msg.equals("OK")){
                aggiungiRecord()
                //Chiusura activity
                finish()
            }
            else {
                messaggioAllerta.setMessage(msg)
                messaggioAllerta.show()
            }
        }
    }

    /**
     * Funzione per salvare i record degli esami nel DB.
     */
    private fun aggiungiRecord() {
        var eTNomeEsame = findViewById<TextView>(R.id.eTNomeEsameM)
        val nomeE = eTNomeEsame.text.toString()

        var tVDaTa = findViewById<TextView>(R.id.tVDataM)
        val dataE = tVDaTa.text.toString()

        var tVOra = findViewById<TextView>(R.id.tVOraM)
        val oraE = tVOra.text.toString()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.addExam(modelExam(null, nomeE, dataE,oraE))
        if (status > -1) {
            Toast.makeText(applicationContext, R.string.esame_inserito_toast, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Funzione che controlla i dati inseriti dall'utente e verifica che non ha lasciato record vuoti
     */
    fun checkDati(nome: String, data: String, ora: String) : String {
        if ((nome.isNotEmpty()) and (data.isEmpty()) and (ora.isNotEmpty())) {
            return "Inserire data esame"
        }
        else if ((nome.isNotEmpty()) and (data.isEmpty()) and (ora.isEmpty())) {
            return "Inserire data e ora esame"
        }
        else if ((nome.isEmpty()) and (data.isEmpty()) and (ora.isEmpty())) {
            return "Inserire nome, data e ora esame"
        }
        else if ((nome.isEmpty()) and (data.isNotEmpty()) and (ora.isNotEmpty())) {
            return "Inserire nome esame"
        }
        else if ((nome.isNotEmpty()) and (data.isNotEmpty()) and (ora.isEmpty())) {
            return "Inserire ora esame"
        }
        else if ((nome.isEmpty()) and (data.isNotEmpty()) and (ora.isEmpty())) {
            return "Inserire nome e ora esame"
        }
        else if ((nome.isEmpty()) and (data.isEmpty()) and (ora.isNotEmpty())) {
            return "Inserire nome e data esame"
        }
        else {
            return "OK"
        }
    }

}

