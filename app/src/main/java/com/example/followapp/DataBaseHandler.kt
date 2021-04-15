package com.example.followapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


// -- Creazione database --

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "DBesami.db"
        private val TABLE_NAME = "Esami"
        private val COLUMN_ID = "_id"
        private val COLUMN_NAME_NOMESAME = "nomeEsame"
        private val COLUMN_NAME_DATA = "dataEsame"
        private val COLUMN_NAME_ORA = "oraEsame"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creazione tabella con campi
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME_NOMESAME + " TEXT,"
                + COLUMN_NAME_DATA + " TEXT" + COLUMN_NAME_ORA + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // -- Funzione per inserire i dati nel DB--

    fun addExam(exam: modelExam): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.COLUMN_ID, exam.id)
        contentValues.put(DatabaseHandler.COLUMN_NAME_NOMESAME, exam.nomeEsame)
        contentValues.put(DatabaseHandler.COLUMN_NAME_DATA, exam.dataEsame)
        contentValues.put(DatabaseHandler.COLUMN_NAME_ORA, exam.oraEsame)


        val success = db.insert(TABLE_NAME, null, contentValues)


        db.close() // Chiusura connessione database
        return success
    }

    // -- Funzione per leggere i dati dal DB in sottoforma di ArrayList
    fun vistaEsami(): ArrayList<modelExam> {

        val examList: ArrayList<modelExam> = ArrayList<modelExam>()

        // Query per selezionare tutti i record dalla tabella
        val selectQuery = "SELECT  * FROM $TABLE_NAME"

        val db = this.readableDatabase
        // Il cursore è usato per leggere i record uno alla volta e aggiungere questi nella classe modello
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var nomeE: String
        var dataE: String
        var oraE: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                nomeE = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NOMESAME))
                dataE = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATA))
                oraE = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ORA))

                val esame = modelExam(id = id, nomeEsame = nomeE, dataEsame = dataE, oraEsame = oraE)
                examList.add(esame)

            } while (cursor.moveToNext())
        }
        return examList
    }


}
