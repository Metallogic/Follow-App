package com.example.followapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 *  Creazione database .
 */

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "DBesami.db"
        private val TABLE_NAME = "Esami"
        private val COLUMN_ID = "_id"
        private val COLUMN_NAME_NOMESAME = "nomeEsame"
        private val COLUMN_NAME_DATA = "dataEsame"
        private val COLUMN_NAME_ORA = "oraEsame"
        private val COLUMN_CESTINO = "cestino"
        private val COLUMN_DATA_ATTUALE = "dataAttuale"
        private val SQL_CreazioneTabella = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME_NOMESAME + " TEXT, "
                + COLUMN_NAME_DATA + " TEXT, " + COLUMN_NAME_ORA + " TEXT, "
                + COLUMN_DATA_ATTUALE + " TEXT, " + COLUMN_CESTINO + " TEXT DEFAULT \"v\" "+ ")")
        private val SQL_DropTable = ("DROP TABLE IF EXISTS $TABLE_NAME")
        private val SQL_selezionaDati1 = ("SELECT * FROM $TABLE_NAME ")
        private val SQL_selezionaDati2 = ("WHERE $COLUMN_NAME_DATA >= ")
        private val SQL_selezionaDati3 = (" ORDER BY $COLUMN_NAME_DATA ASC")
        private val SQL_countdownDay = ("SELECT $COLUMN_NAME_DATA, $COLUMN_DATA_ATTUALE, JULIANDAY($COLUMN_NAME_DATA) - JULIANDAY($COLUMN_DATA_ATTUALE) AS date_difference FROM $TABLE_NAME")


    }
    override fun onCreate(db: SQLiteDatabase?) {
        //creazione tabella con campi
        db?.execSQL(SQL_CreazioneTabella)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DropTable)
        onCreate(db)
    }

    /**
     *  Funzione per inserire i dati nel DB.
     */
    fun addExam(exam: ModelExam): Long {
        val db = this.writableDatabase

        val valoriRow = ContentValues().apply {
            put(COLUMN_NAME_NOMESAME, exam.nomeEsame)
            put(COLUMN_NAME_DATA, exam.dataEsame)
            put(COLUMN_NAME_ORA, exam.oraEsame)
        }

        val success = db.insert(TABLE_NAME, null, valoriRow)
        db.close() // Chiusura connessione database
        return success
    }

    /**
     * Funzione per leggere i dati dal DB sottoforma di ArrayList.
     */
    fun vistaEsami(context: Context, data: String): ArrayList<ModelExam> {
        val query = (SQL_selezionaDati1 + SQL_selezionaDati2 + data + SQL_selezionaDati3)
        val db = this.readableDatabase
        var cursore: Cursor? = null
        val listaEsami = ArrayList<ModelExam>()

        val CursorediffGiorni = db.rawQuery(SQL_countdownDay, null)
        val diffGiorni =CursorediffGiorni.getInt()
        println("@@@ GIORNI DIFFERENZA:" + diffGiorni.toString())

        try {
            cursore = db.rawQuery(query, null)
        }
        catch (e: SQLiteException) {
            db.execSQL(query)
            return ArrayList()
        }

        var idE: Int
        var nomeE: String
        var dataE: String
        var oraE: String
        var cestinoE: String

        if (cursore.moveToFirst()) {
            do {
                idE = cursore.getInt(cursore.getColumnIndex(COLUMN_ID))
                nomeE = cursore.getString(cursore.getColumnIndex(COLUMN_NAME_NOMESAME))
                dataE = cursore.getString(cursore.getColumnIndex(COLUMN_NAME_DATA))
                oraE = cursore.getString(cursore.getColumnIndex(COLUMN_NAME_ORA))
                cestinoE = cursore.getString(cursore.getColumnIndex(COLUMN_CESTINO))

                //Inserimento solo esami con data > alla data attuale
                if (dataE >= data){
                    if (cestinoE.equals("v")) {
                        val exam = ModelExam(id = idE, nomeEsame = nomeE, dataEsame = dataE, oraEsame = oraE)
                        listaEsami.add(exam)
                    }
                }
            } while (cursore.moveToNext())
        }
        cursore.close()
        db.close()
        return listaEsami
    }

    /**
    * Funzione per aggiornare i dati nel DB
    */
    fun updateExam(idEsame: Int, nomeEsame:String, dataEsame:String, oraEsame:String): Int{
        val db = this.writableDatabase
        val valoriRow = ContentValues()
        val success: Int

        //Aggiunta al ContentValues i valori da inserire nel DB per aggiornare riga tramite ID
        valoriRow.put(COLUMN_NAME_NOMESAME, nomeEsame) // ModelExam nome Esame
        valoriRow.put(COLUMN_NAME_DATA, dataEsame) // ModelExam data Esame
        valoriRow.put(COLUMN_NAME_ORA, oraEsame) // ModelExam ora Esame

       try {
            // Aggiornamento riga
            success = db.update(TABLE_NAME, valoriRow, COLUMN_ID + " = " + idEsame, null)
            // Chiusura connessione DB
            db.close()
            return success
       }
       catch (e: Exception){
            // Chiusura connessione DB
            db.close()
            return -1
       }
    }

    /**
     * Funzione per flaggare gli esami eliminati e metterli nel cestino
     */
    fun trashExam(idEsame: Int): Int{
        val db = this.writableDatabase
        val valoriRow = ContentValues()
        val success: Int
        val flagTrash = "c"

        //Aggiunta al ContentValues il valore da inserire nel DB per aggiornare riga tramite ID
        valoriRow.put(COLUMN_CESTINO, flagTrash)

        try {
            // Aggiornamento riga
            success = db.update(TABLE_NAME, valoriRow, COLUMN_ID + " = " + idEsame, null)
            // Chiusura connessione DB
            db.close()
            return success
        }
        catch (e: Exception){
            // Chiusura connessione DB
            db.close()
            return -1
        }
    }

    /**
     * Funzione per ripristinare gli esami cestinati
     */
    fun UnTrashExam(idEsame: Int): Int{
        val db = this.writableDatabase
        val valoriRow = ContentValues()
        val success: Int
        val flagTrash = "v"

        //Aggiunta al ContentValues il valore da inserire nel DB per aggiornare riga tramite ID
        valoriRow.put(COLUMN_CESTINO, flagTrash)

        try {
            // Aggiornamento riga
            success = db.update(TABLE_NAME, valoriRow, COLUMN_ID + " = " + idEsame, null)
            // Chiusura connessione DB
            db.close()
            return success
        }
        catch (e: Exception){
            // Chiusura connessione DB
            db.close()
            return -1
        }
    }

    /**
     * Funzione per eliminare i dati nel DB
     */
    fun deleteExam(idEsame: Int): Int {
        val db = this.writableDatabase
        try {
            // eliminazione dati
            val success = db.delete(TABLE_NAME, COLUMN_ID + " = " + idEsame, null)
            // Chiusura connessione DB
            db.close()
            return success
        }
        catch (e: Exception){
            // Chiusura connessione DB
            db.close()
            return -1
        }
    }

    /**
     * Funzione per leggere i dati dal DB sottoforma di ArrayList.
     */
    fun vistaEsamiVecchi(context: Context, data: String): ArrayList<ModelExam> {
        val query = (SQL_selezionaDati1 + SQL_selezionaDati2 + data + SQL_selezionaDati3)
        val db = this.readableDatabase
        var cursore: Cursor? = null
        val listaEsami = ArrayList<ModelExam>()

        try {
            cursore = db.rawQuery(query, null)
        }
        catch (e: SQLiteException) {
            db.execSQL(query)
            return ArrayList()
        }

        var idE: Int
        var nomeE: String
        var dataE: String
        var oraE: String

        if (cursore.moveToFirst()) {
            do {
                idE = cursore.getInt(cursore.getColumnIndex(COLUMN_ID))
                nomeE = cursore.getString(cursore.getColumnIndex(COLUMN_NAME_NOMESAME))
                dataE = cursore.getString(cursore.getColumnIndex(COLUMN_NAME_DATA))
                oraE = cursore.getString(cursore.getColumnIndex(COLUMN_NAME_ORA))

                //Inserimento solo esami con data > alla data attuale
                if (dataE < data){
                    val exam = ModelExam(id = idE, nomeEsame = nomeE, dataEsame = dataE, oraEsame = oraE)
                    listaEsami.add(exam)
                }
            } while (cursore.moveToNext())
        }
        cursore.close()
        db.close()
        return listaEsami
    }

    /**
     * Funzione per leggere i dati cestinati dal DB sottoforma di ArrayList.
     */
    fun vistaEsamiCestinati(context: Context): ArrayList<ModelExam> {
        val query = (SQL_selezionaDati1)
        val db = this.readableDatabase
        var cursore: Cursor? = null
        val listaEsami = ArrayList<ModelExam>()

        try {
            cursore = db.rawQuery(query, null)
        }
        catch (e: SQLiteException) {
            db.execSQL(query)
            return ArrayList()
        }

        var idE: Int
        var nomeE: String
        var dataE: String
        var oraE: String
        var cestinoE: String

        if (cursore.moveToFirst()) {
            do {
                idE = cursore.getInt(cursore.getColumnIndex(COLUMN_ID))
                nomeE = cursore.getString(cursore.getColumnIndex(COLUMN_NAME_NOMESAME))
                dataE = cursore.getString(cursore.getColumnIndex(COLUMN_NAME_DATA))
                oraE = cursore.getString(cursore.getColumnIndex(COLUMN_NAME_ORA))
                cestinoE = cursore.getString(cursore.getColumnIndex(COLUMN_CESTINO))


                //Inserimento solo esami con data > alla data attuale
                if (cestinoE.equals("c")) {
                    val exam = ModelExam(id = idE, nomeEsame = nomeE, dataEsame = dataE, oraEsame = oraE)
                    listaEsami.add(exam)
                }
            } while (cursore.moveToNext())
        }
        cursore.close()
        db.close()
        return listaEsami
    }

    /**
     * Funzione per aggiornare la date nel DB
     */
    fun updateDate(){
        val db = this.writableDatabase
        val valoriRow = ContentValues()
        val success: Int

        //Lettura data attuale
        val dataAttuale = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MM/yyyy")
        val dataAttualeFormattata = df.format(dataAttuale)

        //Aggiunta al ContentValues i valori da inserire nel DB per aggiornare riga tramite ID
        valoriRow.put(COLUMN_DATA_ATTUALE, dataAttualeFormattata)


        try {
            // Aggiornamento riga
            success = db.update(TABLE_NAME, valoriRow, null, null)
            // Chiusura connessione DB
            db.close()
        }
        catch (e: Exception){
            // Chiusura connessione DB
            db.close()
        }
    }

}

