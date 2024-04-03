package com.example.seba_app_v4.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.seba_app_v4.R
import com.example.seba_app_v4.modeles.Releve

class ReleveAdapter (context : Context, relevebdds: List<Releve>) : ArrayAdapter<Releve>(context, 0 , relevebdds) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val releve = getItem(position)

        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.releve_layout, parent, false)
        }

        val nomTextView: TextView? = itemView?.findViewById(R.id.nomTextView)
        nomTextView?.text = releve?.nom

        return itemView!!
    }
}