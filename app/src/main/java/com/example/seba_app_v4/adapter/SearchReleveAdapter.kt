package com.example.seba_app_v4.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.michael.sqlite.bdd.Releve
import java.util.Locale

class SearchReleveAdapter(
    private val context: Context,
    private var releveList: List<Releve>

) : BaseAdapter(), Filterable {
    private var filteredReleveList = releveList
    override fun getCount(): Int {
        return filteredReleveList.size
    }

    override fun getItem(position: Int): Releve? {
        return filteredReleveList.getOrNull(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        val releve = getItem(position)
        textView.text = releve?.nom ?: ""
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrBlank()) {
                    filterResults.values = releveList
                } else {
                    val searchText = constraint.toString().toLowerCase(Locale.getDefault())
                    val filteredList = releveList.filter {
                        it.nom?.toLowerCase(Locale.getDefault())?.contains(searchText) == true
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredReleveList = results?.values as? List<Releve> ?: listOf()
                notifyDataSetChanged()
            }
        }
    }
}