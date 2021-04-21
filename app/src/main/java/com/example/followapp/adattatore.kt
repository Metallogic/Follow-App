package com.example.followapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class examAdapter (val exam: ArrayList<modelExam>): RecyclerView.Adapter<examAdapter.ViewHolder>(){

    companion object{
        val ID_ESAME = "COLUMN_ID"
        val NOME_ESAME = "COLUMN_NAME_NOMESAME"
        val DATA_ESAME = "COLUMN_NAME_DATA"
        val ORA_ESAME ="COLUMN_NAME_ORA"
    }

    class ViewHolder(itemView: View, ): RecyclerView.ViewHolder(itemView){
        val tVnomeEsame: TextView
        val tVdataEsame: TextView
        val iVmodifyB : ImageButton

        //Inizializzazione
        init {
            //Inizializzazione itemView: TextView e Botton
            tVnomeEsame = itemView.findViewById(R.id.tVnomeEsameRow)
            tVdataEsame = itemView.findViewById(R.id.tVdataEsameRow)
            iVmodifyB = itemView.findViewById(R.id.modifyB)

            //Inizializzazione click sul bottone di modifica
            iVmodifyB.setOnClickListener {
                val intent = Intent(itemView.context, ModificaEsami::class.java)

                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder{

        val v: View = LayoutInflater.from(p0.context).inflate(R.layout.exam_row, p0, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int{
        return exam.size

    }

    override fun onBindViewHolder(p0:examAdapter.ViewHolder, p1: Int) {
        val esame: modelExam = exam[p1]
        p0.tVnomeEsame.text = esame.nomeEsame
        p0.tVdataEsame.text = esame.dataEsame
    }

}
