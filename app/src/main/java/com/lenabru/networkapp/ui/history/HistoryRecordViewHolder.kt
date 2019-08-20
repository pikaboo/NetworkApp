package com.lenabru.networkapp.ui.history

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lenabru.networkapp.R
import com.lenabru.networkapp.database.models.HistoryRecord
import java.text.SimpleDateFormat


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

class HistoryRecordViewHolder(view: View, private val dateFormatter: SimpleDateFormat) :
    RecyclerView.ViewHolder(view) {

    private val record: TextView = view.findViewById(R.id.historyRecord)

    fun bind(historyRecord: HistoryRecord) {
        record.text = itemView.context.getString(
            R.string.from_network_to_network_format,
            dateFormatter.format(historyRecord.timestamp),
            historyRecord.fromNetwork,
            historyRecord.toNetwork
        )
    }

}