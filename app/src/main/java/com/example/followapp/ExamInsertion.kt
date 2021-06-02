package com.example.followapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
         * -- GESTIONE EDIT-TEXT NOME ESAME --
         */
        val eTNomeEsame = findViewById<TextView>(R.id.eTNomeEsameM)

        /**
         * -- GESTIONE CALENDARIO --
         */
        //Crezione val per textView e inizializzazione per inserimento data
        val tVDaTa = findViewById<TextView>(R.id.tVDataM)
        //Calendario scelta data
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)
        //Crezione bottone e inizializzazione per inserimento data
        val dataButton = findViewById<Button>(R.id.dataB)
        //Evento click dataB in cui viene aperto il calendario
        dataButton.setOnClickListener {
            val dataCalendario = DatePickerDialog(this, { view, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                var giorno: String = mDayOfMonth.toString()
                val mesePlus1 = mMonth + 1
                var mese: String = mesePlus1.toString()
                if (mDayOfMonth<10){
                    giorno = "0"+giorno
                }
                if (mMonth<10){
                    mese = "0"+mese
                }
                tVDaTa.setText("" + mYear + "-" + mese + "-" + giorno)
                }, year, month, day)
            dataCalendario.show()
            //Visualizzazione Giorni mancanti all'esame
        }


        /**
         * -- GESTIONE OROLOGIO --
         */
        //Orologio scelta ora
        val orologio = Calendar.getInstance()
        //Crezione val per textView e inizializzazione per inserimento ora
        val tVOrA = findViewById<TextView>(R.id.tVOraM)
        //Crezione bottone e inizializzazione per inserimento ora
        val oraButton = findViewById<Button>(R.id.oraB)
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
        val salvataggioButton = findViewById<Button>(R.id.salvaB)
        //Evento click salvaB in cui vengono salvati i dati inserite dall'utente
        salvataggioButton.setOnClickListener {
            // Creazione messaggio allerta se dati non inseriti
            val messaggioAllerta = AlertDialog.Builder(this@ExamInsertion)
            messaggioAllerta.setTitle(getString(R.string.attenzione_msg))
            messaggioAllerta.setMessage(getString(R.string.dati_incompleti))
            messaggioAllerta.setPositiveButton(getString(R.string.OK)) { dialog, id, ->
                dialog.dismiss()
            }
            //Controllo dati inseriti, se incompleti messagio di errore, altrimenti toast di salvataggio andato a buon fine

            val msg= checkDati(eTNomeEsame.getText().toString(),tVDaTa.getText().toString(),tVOrA.getText().toString(),)
            //Inserimento dati nel DB e Toast di avvenuto inserimento esame
            if(msg.equals(getString(R.string.OK))){
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
        val eTNomeEsame = findViewById<TextView>(R.id.eTNomeEsameM)
        val nomeE = eTNomeEsame.text.toString()

        val tVDaTa = findViewById<TextView>(R.id.tVDataM)
        val dataE = tVDaTa.text.toString()

        val tVOra = findViewById<TextView>(R.id.tVOraM)
        val oraE = tVOra.text.toString()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.addExam(ModelExam(null, nomeE, dataE,oraE))
        if (status > -1) {
            Toast.makeText(applicationContext, R.string.esame_inserito_toast, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Funzione che controlla i dati inseriti dall'utente e verifica che non abbia lasciato record vuoti
     */
    fun checkDati(nome: String, data: String, ora: String) : String {
        var errMsg = ""

        if (checkInsertName(nome)!=true){
            errMsg += "\n" + getString(R.string.inserire_nomeE)
        }
        if (checkInsertData(data)!=true){
            errMsg += "\n" + getString(R.string.inserire_dataE)
        }
        if (checkInsertOra(ora)!=true){
            errMsg += "\n" + getString(R.string.inserire_oraE)
        }
        if (checkLengthNome(nome)!=true){
            errMsg += "\n" + getString(R.string.inserire_nome_esame_max_30_caratteri)
        }
        if (checkData(data)!=true){
            errMsg += "\n" + getString(R.string.inserire_data_maggiore)
        }
        if (checkOra(data, ora)!=true){
            errMsg += "\n" + getString(R.string.inserire_ora_maggiore)
        }

        if (!errMsg.equals("")){
            return errMsg
        }
        return "OK"
    }

    /**
     * Funzione che controlla la lunghezza massima del nome esame inserito nella view
     */
    fun checkExamName(v: View?) {
        val eTNomeEsame = findViewById<TextView>(R.id.eTNomeEsameM)
        val nomeE = eTNomeEsame.text.toString()
        if (nomeE.length >= 30) {
            //Set messaggio di errore per nome inserito troppo lungo
            eTNomeEsame.setError(getString(R.string.MaxNomeE))
        }
    }

    /**
     * Funzione che controlla se si è inserita una data
     */
    fun checkInsertData(dataE: String): Boolean {
        if (dataE.isNotEmpty()) {
            return true
        }
        return false
    }

    /**
     * Funzione che controlla se si è dato un nome all'esame
     */
    fun checkInsertName(nomeE: String): Boolean {
        if (nomeE.isNotEmpty()) {
            return true
        }
        return false
    }

    /**
     * Funzione che controlla se si è inserita un'ora
     */
    fun checkInsertOra(oraE: String): Boolean {
        if (oraE.isNotEmpty()) {
            return true
        }
        return false
    }

    /**
     * Funzione che controlla la lunghezza max del nome esame inserito
     */
    fun checkLengthNome(nomeE: String): Boolean{
        if (nomeE.length <= 30) {
            return true
        }
        return false
    }


    /**
     * Funzione che controlla che la data inserita dall'utente in fase di inserimento di un nuovo esame,
     * sia maggiore o uguale della data attuale
     */
    fun checkData(dataE: String): Boolean {
        //Lettura data attuale
        val dataAttuale = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd")
        val dataAttualeFormattata = df.format(dataAttuale)

        if (dataE.isNotEmpty()){
            //Scomposizione data Esame
            val annoE = dataE.subSequence(0, 4).toString()
            val meseE = dataE.subSequence(5, 7).toString()
            val giornoE = dataE.subSequence(8, 10).toString()
            //Scomposizione data attuale
            val annoA = dataAttualeFormattata.subSequence(0, 4).toString()
            val meseA = dataAttualeFormattata.subSequence(5, 7).toString()
            val giornoA = dataAttualeFormattata.subSequence(8, 10).toString()

            if (annoE.toInt() > annoA.toInt()) {
                return true
            }
            if (annoE.toInt() == annoA.toInt()) {
                if (meseE.toInt() > meseA.toInt()) {
                    return true
                }
            }
            if (annoE.toInt() == annoA.toInt()) {
                if (meseE.toInt() == meseA.toInt()) {
                    if (giornoE.toInt() >= giornoA.toInt()) {
                        return true //data e ora >= dell'attuale
                    }
                }
            }
        }
        return false //data < dell'attuale
    }

    /**
     * Funzione che controlla che l'ora inserita dall'utente in fase di inserimento di un nuovo esame,
     * sia maggiore o uguale dell'ora attuale
     */
    fun checkOra(dataE: String, oraE: String): Boolean {
        //Lettura data attuale
        val oraAttuale = Calendar.getInstance().time
        val oraDf = SimpleDateFormat("HH:mm")
        val oraAttualeFormattata = oraDf.format(oraAttuale)
        //Lettura data attuale
        val dataAttuale = Calendar.getInstance().time
        val dataDf = SimpleDateFormat("yyyy-MM-dd")
        val dataAttualeFormattata = dataDf.format(dataAttuale)

        if (oraE.isNotEmpty() && dataE.isNotEmpty()) {
            //Scomposizione data Esame
            val annoE = dataE.subSequence(0, 4).toString()
            val meseE = dataE.subSequence(5, 7).toString()
            val giornoE = dataE.subSequence(8, 10).toString()
            //Scomposizione data attuale
            val annoA = dataAttualeFormattata.subSequence(0, 4).toString()
            val meseA = dataAttualeFormattata.subSequence(5, 7).toString()
            val giornoA = dataAttualeFormattata.subSequence(8, 10).toString()
            //Scomposizione ora Esame
            val oRaE = oraE.subSequence(0, 2).toString()
            val minE = oraE.subSequence(3, 5).toString()
            //Scomposizione ora attuale
            val oraA = oraAttualeFormattata.subSequence(0, 2).toString()
            val minA = oraAttualeFormattata.subSequence(3, 5).toString()

            if (annoE.toInt() == annoA.toInt()) {
                if (meseE.toInt() == meseA.toInt()) {
                    if (giornoE.toInt() == giornoA.toInt()) {
                        if (oRaE.toInt() > oraA.toInt()) {
                            return true
                        }
                    }
                }
            }
            if (annoE.toInt() == annoA.toInt()) {
                if (meseE.toInt() == meseA.toInt()) {
                    if (giornoE.toInt() == giornoA.toInt()) {
                        if (oRaE.toInt() == oraA.toInt()) {
                            if (minE.toInt() >= minA.toInt()) {
                                return true
                            }
                        }
                    }
                }
            }
            else return false
        }
        return false //ora < dell'attuale
    }
}


