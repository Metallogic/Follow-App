package com.example.followapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class examAdapter (val exam: ArrayList<modelExam>): RecyclerView.Adapter<examAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tVnomeEsame: TextView
        val tVdataEsame: TextView
        val iVmodifyB : ImageButton

        init{
            tVnomeEsame = itemView.findViewById(R.id.tVnomeEsameRow)
            tVdataEsame = itemView.findViewById(R.id.tVdataEsameRow)
            iVmodifyB = itemView.findViewById(R.id.modifyB)
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