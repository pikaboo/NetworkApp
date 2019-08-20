package com.lenabru.networkapp.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lenabru.networkapp.R
import com.lenabru.networkapp.database.models.HistoryRecord
import java.text.SimpleDateFormat


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

class HistoryRecordAdapter(private val dateFormatter: SimpleDateFormat) :
    RecyclerView.Adapter<HistoryRecordViewHolder>() {

    private val currentHistory = mutableListOf<HistoryRecord>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryRecordViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_history, parent, false)
        return HistoryRecordViewHolder(view, dateFormatter)
    }

    override fun getItemCount(): Int {
        return currentHistory.count()
    }

    override fun onBindViewHolder(holder: HistoryRecordViewHolder, position: Int) {
        val record = currentHistory[position]
        holder.bind(record)
    }

    fun setHistory(history: List<HistoryRecord>) {
        currentHistory.clear()
        currentHistory.addAll(history)
        notifyDataSetChanged()
    }

}