package com.lenabru.networkapp.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

@Entity(tableName = "HistoryTable")
class HistoryRecord(
    @PrimaryKey @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "fromNetwork") val fromNetwork: String,
    @ColumnInfo(name = "toNetwork") val toNetwork: String,
    @ColumnInfo(name = "sentToServer") var sentToServer: Boolean = false
) {

    companion object {
        const val NoConnectivity = "No Connectivity"
        const val Wifi = "WIFI"
        const val Mobile = "Cellular"
    }

    override fun equals(other: Any?): Boolean {
        (other as? HistoryRecord)?.let {
            return timestamp == it.timestamp
        }

        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(timestamp, fromNetwork, toNetwork, sentToServer)
    }

    override fun toString(): String {
        return "$timestamp - $fromNetwork - $toNetwork"
    }
}