package com.lenabru.networkapp.network

import android.util.Log
import androidx.annotation.WorkerThread
import com.lenabru.networkapp.api.ApiService
import com.lenabru.networkapp.api.models.ServerUpdate
import com.lenabru.networkapp.api.models.WifiAlert
import com.lenabru.networkapp.database.HistoryDAO
import com.lenabru.networkapp.database.models.HistoryRecord
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

class NetworkStatusRepository(private val dao: HistoryDAO, private val service: ApiService) :
    CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job


    @WorkerThread
    suspend fun onNetworkStatusChanged(status: Int) = withContext(coroutineContext) {
        val previousRecord: HistoryRecord? = dao.getLatestRecord().firstOrNull()
        val historyRecord = createNewRecord(status, previousRecord)
        dao.insert(historyRecord)
    }

    fun createNewRecord(status: Int, previousRecord: HistoryRecord?): HistoryRecord {
        val timestamp = System.currentTimeMillis()
        val fromNetwork = previousRecord?.toNetwork ?: HistoryRecord.NoConnectivity
        return HistoryRecord(
            timestamp,
            fromNetwork,
            when (status) {
                NetworkStatus.TYPE_WIFI -> HistoryRecord.Wifi
                NetworkStatus.TYPE_MOBILE -> HistoryRecord.Mobile
                else -> HistoryRecord.NoConnectivity
            }
        )
    }

    suspend fun sendServerUpdates(records: List<HistoryRecord>) = withContext(coroutineContext) {
        records.forEach {
            val historyBody = ServerUpdate(it.fromNetwork, it.toNetwork)
            try {
                val response = service.sendServerUpdate(historyBody).execute()
                if (response.isSuccessful) {
                    it.sentToServer = true
                    Log.d("Update", "Updating record $it")
                    dao.update(it)
                } else {
                    //If the most recent request was not successful, cancel the rest of it,
                    //and try again at the next iteration
                    this.cancel()
                }
            } catch (e: Exception) {
                //if there was an exception cancel the job
                this.cancel()
                Log.e("NetworkStatusRepository", "Error Canceling server updates", e)
            } finally {
            }

        }
    }

    @WorkerThread
    suspend fun sendWifiAlert(wifiName: String, connectedOn: String) =
        withContext(coroutineContext) {
            val wifiAlert = WifiAlert(wifiName, connectedOn)
            val response = service.sendWifiAlert(wifiAlert).execute()
            if (response.isSuccessful) {
                Log.d("WifiAlert", "Updated")
            }
        }
}