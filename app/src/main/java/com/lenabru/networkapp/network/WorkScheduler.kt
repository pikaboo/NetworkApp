package com.lenabru.networkapp.network

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.lenabru.networkapp.network.background.NetworkWork
import java.util.*
import java.util.concurrent.TimeUnit


/* 
 * Created by Lena Brusilovski on 2019-08-20
 */

object WorkScheduler {
    private const val wifiAlertTag = "WifiAlertWorker"
    private lateinit var wifiAlertTask: WifiAlertTask
    /**
     * The minimum periodic innterval between
     * tasks in the work manager is 15 minutes, so
     * this method does not satisfy the needs of the spec
     * I'm leaving this here for reference, it was possible to just schedule this
     * and it would work in the background as well, but the time interval is minimum 900 seconds
     */
    fun scheduleWifiAlertChecks() {
        val requestBuilder =
            PeriodicWorkRequest.Builder(NetworkWork::class.java, 15, TimeUnit.MINUTES)
        val request = requestBuilder.build()
        val workManager = WorkManager.getInstance()
        workManager.enqueueUniquePeriodicWork(
            wifiAlertTag,
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    fun scheduleWifiAlertChecksInForeground(context: Context) {
        val timer = Timer()
        wifiAlertTask = WifiAlertTask(context)
        timer.scheduleAtFixedRate(
            object : TimerTask() {

                override fun run() {
                    Log.d("WifiAlert", "executing wifi alert task")
                    wifiAlertTask.perform()
                }

            },
            0,
            TimeUnit.MINUTES.toMillis(5)
        )
    }
}