package com.example.followapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


class ModificaEsami : AppCompatActivity() {

    companion object {
        val ID_ESAME = "COLUMN_ID"
        val NOME_ESAME = "COLUMN_NAME_NOMESAME"
        val DATA_ESAME = "COLUMN_NAME_DATA"
        val ORA_ESAME = "COLUMN_NAME_ORA"
        val COUNTDOWN = "COUNTDOWN_VALOR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifica_esami)

        val nomeE = findViewById<EditText>(R.id.eTNomeEsameM)
        val dataE = findViewById<TextView>(R.id.tVDataM)
        val oraE = findViewById<TextView>(R.id.tVOraM)
        val countDownDay = findViewById<TextView>(R.id.CountDownDay)

        /**
         * Settaggio dati nell'activity
         */
        //Get variabili passati dall'intent
        val intent = intent
        val idEsame = intent.getIntExtra(ID_ESAME, -1)
        val nomeEsame = intent.getStringExtra(NOME_ESAME)
        val dataEsame = intent.getStringExtra(DATA_ESAME)
        val oraEsame = intent.getStringExtra(ORA_ESAME)
        val CountDay = intent.getIntExtra(COUNTDOWN, -1)

        //Settaggio valori nellle EditText e TextView
        nomeE.setText(nomeEsame)
        dataE.setText(dataEsame)
        oraE.setText(oraEsame)
        countDownDay.setText(CountDay.toString())

        //Calendario scelta data
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)


        /**
         * -- GESTIONE CALENDARIO --
         */
        //Crezione bottone e inizializzazione per inserimento data
        val dataButton = findViewById<Button>(R.id.dataB)
        //Evento click dataB in cui viene aperto il calendario
        dataButton.setOnClickListener {
            val dataCalendario = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                var giorno: String = mDayOfMonth.toString()
                val mesePlus1 = mMonth + 1
                var mese: String = mesePlus1.toString()
                if (mDayOfMonth<10){
                    giorno = "0"+giorno
                }
                if (mMonth<10){
                    mese = "0"+mese
                }
                dataE.setText("" + mYear + "-" + mese + "-" + giorno)
            }, year, month, day)
            dataCalendario.show()
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
         * -- GESTIONE ANNULLAMENTO OPERAZIONE --
         */
        //Crezione bottone e inizializzazione annullaB
        val annullaOperazione = findViewById<Button>(R.id.annullaB)
        //Evento click annullaB in cui viene annulla l'operazione di modifica esame e ritorno al main activity
        annullaOperazione.setOnClickListener {
            Toast.makeText(applicationContext, R.string.operazione_annullata, Toast.LENGTH_SHORT).show()
            //Chiusura activity
            finish()
        }

        /**
         * -- GESTIONE SALVATAGGIO MODIFICHE --
         */
        //Avviso di sicurezza
        val avvisoS = AlertDialog.Builder(this)
        avvisoS.setTitle(getString(R.string.attenzione_msg))
        avvisoS.setMessage(getString(R.string.sicuro_modifiche_msg))
        avvisoS.setPositiveButton(getString(R.string.SI)){ _, _ ->
            modificaRiga(idEsame)
            finish()
        }
        avvisoS.setNegativeButton(getString(R.string.NO)){ _, _ ->
            //linea vuota, la finiestra di dialogo si chiude
        }

        //Crezione bottone e inizializzazione salvaB
        val saveChangeB = findViewById<Button>(R.id.salvaB)
        //Evento click salvaB in cui vengono modificati i dati dell'esame
        saveChangeB.setOnClickListener {
            // Creazione messaggio allerta se dati non inseriti
            val messaggioAllerta = AlertDialog.Builder(this@ModificaEsami)
            messaggioAllerta.setTitle(getString(R.string.attenzione_msg))
            messaggioAllerta.setMessage(getString(R.string.dati_incompleti))
            messaggioAllerta.setPositiveButton(getString(R.string.OK)) { dialog, id, ->
                dialog.dismiss()
            }
            //Controllo dati inseriti, se incompleti messagio di errore, altrimenti creazione toast di salvataggio andato a buon fine
            val msg= checkDati(nomeE.getText().toString(),dataE.getText().toString(),oraE.getText().toString(),)
            //Inserimento dati nel DB e Toast di avvenuto inserimento esame
            if(msg.equals("OK")){
                avvisoS.show()
            }
            else {
                messaggioAllerta.setMessage(msg)
                messaggioAllerta.show()
            }
        }

        /**
         * -- GESTIONE ELIMINAZIONE ESAME --
         */
        //Avviso di sicurezza
        val avvisoD = AlertDialog.Builder(this)
        avvisoD.setTitle(getString(R.string.dati_incompleti))
        avvisoD.setMessage(getString(R.string.sicuro_eliminare_msg))
        avvisoD.setPositiveButton(getString(R.string.SI)){ _, _ ->
            //Chiamata funzione per eliminare l'esame e aggiornare lo stato del DB
            eliminaRiga(idEsame)
            finish()
        }
        avvisoD.setNegativeButton(getString(R.string.NO)){ _, _ ->
            //linea vuota, la finiestra di dialogo si chiude
        }
        //Crezione bottone e inizializzazione deleteB
        val deleteB = findViewById<Button>(R.id.deleteBtrash)
        //Evento click deleteB in cui vengono eliminati i dati dell'esame
        deleteB.setOnClickListener {
            avvisoD.show()
        }
    }

    /**
     * Funzione che permette la modifica della riga relativa all'esame in oggetto
     */
    fun modificaRiga(idEsame: Int) {
        //Inizializzazione campi da modificare
        val eTNomeEsame = findViewById<EditText>(R.id.eTNomeEsameM)
        val nomeE = eTNomeEsame.text.toString()

        val tVDaTa = findViewById<TextView>(R.id.tVDataM)
        val dataE = tVDaTa.text.toString()

        val tVOra = findViewById<TextView>(R.id.tVOraM)
        val oraE = tVOra.text.toString()

        //Aggiornamento DB con nuovi valori
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.updateExam(idEsame, nomeE, dataE, oraE)
        if (status > -1) {
            Toast.makeText(applicationContext, R.string.esame_modificato_toast, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, R.string.errore_esame, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Funzione che permette l'eliminazione della riga relativa all'esame in oggetto
     */
    fun eliminaRiga(idEsame: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.trashExam(idEsame)
        if (status > -1) {
            Toast.makeText(applicationContext, R.string.esame_cestinato_toast, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, R.string.errore_esame, Toast.LENGTH_LONG).show()
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
     * Funzione che controlla che la data inserita dall'uenete in fase di inserimento di un nuovo esame,
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
            //Scomposizione data Esame
            val annoA = dataAttualeFormattata.subSequence(0, 4).toString()
            val meseA = dataAttualeFormattata.subSequence(5, 7).toString()
            val giornoA = dataAttualeFormattata.subSequence(8, 10).toString()

            if (annoE.toInt() >= annoA.toInt()) {
                if (meseE.toInt() >= meseA.toInt()) {
                    if (giornoE.toInt() >= giornoA.toInt()) {
                        return true //data >= dell'attuale
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