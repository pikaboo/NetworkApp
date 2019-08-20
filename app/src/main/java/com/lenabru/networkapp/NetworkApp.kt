package com.lenabru.networkapp

import android.app.Application
import com.lenabru.networkapp.network.WorkScheduler


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */
//it looks like this class isnt used, but it is used in the manifest
class NetworkApp : Application() {

    override fun onCreate() {
        super.onCreate()
        WorkScheduler.scheduleWifiAlertChecksInForeground(applicationContext)

    }


}