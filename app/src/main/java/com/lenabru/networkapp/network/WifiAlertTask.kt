package com.lenabru.networkapp.network

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import com.lenabru.networkapp.api.Api
import com.lenabru.networkapp.database.HistoryDatabase
import com.lenabru.networkapp.database.models.HistoryRecord
import com.lenabru.networkapp.repo.HistoryRepository
import com.lenabru.networkapp.repo.isNetworkSwitch
import com.lenabru.networkapp.repo.isOnSameNetwork
import com.lenabru.networkapp.viewmodel.LifeCycleOwnerImpl
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat


/* 
 * Created by Lena Brusilovski on 2019-08-20
 */

class WifiAlertTask(val context: Context) {
    private val historyDAO = HistoryDatabase.getDatabase(context).historyDao()
    private val lifecycleOwner = LifeCycleOwnerImpl()
    private val historyRepository: HistoryRepository by lazy {
        HistoryRepository(historyDAO)
    }

    private var lastNetworkConnectedTo: HistoryRecord? = null
    private val networkStatusRepository: NetworkStatusRepository by lazy {
        NetworkStatusRepository(historyDAO, Api.service)
    }

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")


    var networkStatusManager: INetworkStatusManager = NetworkStatusManager()

    init {
        historyRepository.getLiveLatestRecord().observe(lifecycleOwner, Observer {
            val newRecord = it.firstOrNull()
            if (lastNetworkConnectedTo.isNetworkSwitch(newRecord)) {
                lastNetworkConnectedTo = newRecord
            }
        })
    }

    private fun shouldSendWifiAlert(oldRecord: HistoryRecord?, newRecord: HistoryRecord?): Boolean {
        return oldRecord.isOnSameNetwork(newRecord)
    }

    fun perform() {
        Log.d("WifiAlert", "Starting periodic worker")
        runBlocking {
            withContext(coroutineContext) {
                val lastRecord = historyRepository.getLatestRecord().firstOrNull()
                val connectivityStatus =
                    networkStatusManager.getConnectivityStatus(context.applicationContext)
                val historyRecord =
                    networkStatusRepository.createNewRecord(connectivityStatus, lastRecord)
                if (shouldSendWifiAlert(lastNetworkConnectedTo, historyRecord)) {
                    Log.d("WifiAlert", "Sending wifi alert")
                    val timeStamp = dateFormatter.format(
                        lastNetworkConnectedTo?.timestamp ?: historyRecord.timestamp
                    )
                    networkStatusRepository.sendWifiAlert(
                        historyRecord.toNetwork,
                        timeStamp
                    )
                }
            }
        }
    }

}