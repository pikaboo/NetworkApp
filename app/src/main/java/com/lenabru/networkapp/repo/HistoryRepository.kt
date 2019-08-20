package com.lenabru.networkapp.repo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.lenabru.networkapp.database.HistoryDAO
import com.lenabru.networkapp.database.models.HistoryRecord


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

class HistoryRepository(private val dao: HistoryDAO) {


    @WorkerThread
    suspend fun insert(historyRecord: HistoryRecord) {
        dao.insert(historyRecord)
    }

    @WorkerThread
    suspend fun update(historyRecord: HistoryRecord) {
        dao.update(historyRecord)
    }


    fun getHistory(): LiveData<List<HistoryRecord>> {
        return dao.getHistory()
    }

    @WorkerThread
    suspend fun getLatestRecord(): List<HistoryRecord> {
        return dao.getLatestRecord()
    }

    @WorkerThread
    fun getUnsentRecords(): LiveData<List<HistoryRecord>> {
        return dao.getUnsentRecords()
    }

    fun getLiveLatestRecord(): LiveData<List<HistoryRecord>> {
        return dao.getLiveLatestRecord()
    }

}

fun HistoryRecord?.isOnSameNetwork(newstRecord: HistoryRecord?): Boolean {
    if (this == null) {
        return false
    }
    return this.toNetwork == newstRecord?.toNetwork && newstRecord?.fromNetwork == newstRecord?.toNetwork
}

fun HistoryRecord?.isNetworkSwitch(newstRecord: HistoryRecord?): Boolean {
    if ((this == null && newstRecord != null) || (this != null && newstRecord == null)) {
        return true
    }
    return this?.toNetwork == newstRecord?.fromNetwork && newstRecord?.fromNetwork != newstRecord?.toNetwork
}