package com.example.followapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
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
        private val SQL_CreazioneTabella = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME_NOMESAME + " TEXT, "
                + COLUMN_NAME_DATA + " TEXT, " + COLUMN_NAME_ORA + " TEXT" + ")")
        private val SQL_DropTable = ("DROP TABLE IF EXISTS $TABLE_NAME")
        private val SQL_selezionaDati = ("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_NAME_DATA ASC")


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
    fun vistaEsami(context: Context): ArrayList<ModelExam> {

        val query = (SQL_selezionaDati)
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

                val exam = ModelExam(id = idE, nomeEsame = nomeE, dataEsame = dataE, oraEsame = oraE)
                listaEsami.add(exam)

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
     * Funzione per eliminare i dati nel DB con data passata
     */
    fun deleteDataExam(dataEsame: String){
        val db = this.writableDatabase
        try {
            // eliminazione dati
            db.delete(TABLE_NAME, COLUMN_NAME_DATA + " < " + dataEsame, null)
            // Chiusura connessione DB
            db.close()
        }
        catch (e: Exception){
            // Chiusura connessione DB
            db.close()
        }
    }

}

