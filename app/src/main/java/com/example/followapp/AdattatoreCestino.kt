package com.example.followapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class trashAdapter (val exam: ArrayList<ModelExam>, val context: Context): RecyclerView.Adapter<trashAdapter.ViewHolder>() {

    companion object {
        val ID_ESAME = "COLUMN_ID"
        val NOME_ESAME = "COLUMN_NAME_NOMESAME"
        val DATA_ESAME = "COLUMN_NAME_DATA"
        val ORA_ESAME = "COLUMN_NAME_ORA"
    }

    class ViewHolder(itemView: View, var esame: ModelExam? = null) :
        RecyclerView.ViewHolder(itemView) {
        val tVnomeEsame: TextView
        val tVdataEsame: TextView
        val tVoraEsame: TextView

        //Inizializzazione
        init {
            //Inizializzazione itemView: TextView e Botton
            tVnomeEsame = itemView.findViewById(R.id.tVnomeEsameRow)
            tVdataEsame = itemView.findViewById(R.id.tVdataEsameRow)
            tVoraEsame = itemView.findViewById(R.id.tVoraEsameRow)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v: View = LayoutInflater.from(p0.context).inflate(R.layout.exam_row, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return exam.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val esame: ModelExam = exam[position]
        holder.tVnomeEsame.text = esame.nomeEsame
        holder.tVdataEsame.text = esame.dataEsame
        holder.tVoraEsame.text = esame.oraEsame

        //Al click dell'oggetto esame vengono passati i valori tramite intent
        holder.itemView.setOnClickListener {
            val modello = exam.get(position)
            //Inizializzazione valori da passare
            var getID: Int? = modello.id
            var getNomeEsame: String? = modello.nomeEsame
            var getDataEsame: String? = modello.dataEsame
            var getOraEsame: String? = modello.oraEsame

            val intent = Intent(context, EliminaEsamiCestino::class.java)
            //Passaggio valori
            intent.putExtra(ID_ESAME, getID)
            intent.putExtra(NOME_ESAME, getNomeEsame)
            intent.putExtra(DATA_ESAME, getDataEsame)
            intent.putExtra(ORA_ESAME, getOraEsame)

            context.startActivity(intent)
        }
    }
}
