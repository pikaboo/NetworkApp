package com.lenabru.networkapp.network.background

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lenabru.networkapp.network.WifiAlertTask
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


/* 
 * Created by Lena Brusilovski on 2019-08-20
 */

class NetworkWork(val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters), CoroutineScope {

    private var job: Job = Job()

    private val wifiAlertTask = WifiAlertTask(context)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job


    override fun doWork(): Result {
        wifiAlertTask.perform()
        return Result.success()
    }

}