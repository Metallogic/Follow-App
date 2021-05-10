package com.example.followapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifica_esami)

        var nomeE = findViewById<EditText>(R.id.eTNomeEsameM)
        var dataE = findViewById<TextView>(R.id.tVDataM)
        var oraE = findViewById<TextView>(R.id.tVOraM)

        /**
         * Settaggio dati nell'activity
         */
        //Get variabili passati dall'intent
        val intent = intent
        val idEsame = intent.getIntExtra(ID_ESAME, -1)
        val nomeEsame = intent.getStringExtra(NOME_ESAME)
        val dataEsame = intent.getStringExtra(DATA_ESAME)
        val oraEsame = intent.getStringExtra(ORA_ESAME)

        //Settaggio valori nellle EditText e TextView
        nomeE.setText(nomeEsame)
        dataE.setText(dataEsame)
        oraE.setText(oraEsame)

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
                dataE.setText("" + giorno + "/" + mese + "/" + mYear)
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
        avvisoS.setTitle("ATTENZIONE")
        avvisoS.setMessage("Sicuro di voler apportare le modifiche all'esame?")
        avvisoS.setPositiveButton("SI"){ _, _ ->
            modificaRiga(idEsame)
            finish()
        }
        avvisoS.setNegativeButton("NO"){ _, _ ->
            //linea vuota, la finiestra di dialogo si chiude
        }

        //Crezione bottone e inizializzazione salvaB
        val saveChangeB = findViewById<Button>(R.id.salvaB)
        //Evento click salvaB in cui vengono modificati i dati dell'esame
        saveChangeB.setOnClickListener {
            // Creazione messaggio allerta se dati non inseriti
            val messaggioAllerta = AlertDialog.Builder(this@ModificaEsami)
            messaggioAllerta.setTitle("ATTENZIONE")
            messaggioAllerta.setMessage("Dati inseriti incompleti!")
            messaggioAllerta.setPositiveButton("OK") { dialog, id, ->
                dialog.dismiss()
            }
            //Controllo dati inseriti, se incompleti messagio di errore, altrimenti toast di salvataggio andato a buon fine
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
        avvisoD.setTitle("ATTENZIONE")
        avvisoD.setMessage("Sicuro di voler eliminare l'esame?")
        avvisoD.setPositiveButton("SI"){ _, _ ->
            //Chiamata funzione per eliminare l'esame e aggiornare lo stato del DB
            eliminaRiga(idEsame)
            finish()
        }
        avvisoD.setNegativeButton("NO"){ _, _ ->
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
        var eTNomeEsame = findViewById<EditText>(R.id.eTNomeEsameM)
        val nomeE = eTNomeEsame.text.toString()

        var tVDaTa = findViewById<TextView>(R.id.tVDataM)
        val dataE = tVDaTa.text.toString()

        var tVOra = findViewById<TextView>(R.id.tVOraM)
        val oraE = tVOra.text.toString()

        //Aggiornamento DB con nuovi valori
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.updateExam(idEsame, nomeE, dataE, oraE)
        if (status > -1) {
            Toast.makeText(applicationContext, R.string.esame_cestinato_toast, Toast.LENGTH_LONG).show()
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