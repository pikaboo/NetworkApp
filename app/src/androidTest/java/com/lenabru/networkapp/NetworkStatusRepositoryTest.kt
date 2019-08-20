package com.lenabru.networkapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.lenabru.networkapp.api.Api
import com.lenabru.networkapp.database.HistoryDAO
import com.lenabru.networkapp.database.HistoryDatabase
import com.lenabru.networkapp.database.models.HistoryRecord
import com.lenabru.networkapp.network.NetworkStatus
import com.lenabru.networkapp.network.NetworkStatusRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.coroutines.CoroutineContext


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

class NetworkStatusRepositoryTest : CoroutineScope {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private lateinit var networkRepository: NetworkStatusRepository

    private lateinit var db: HistoryDatabase

    private lateinit var historyDao: HistoryDAO

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java).build()
        historyDao = db.historyDao()
        networkRepository = NetworkStatusRepository(historyDao, Api.service)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        db.close()
    }

    @Test
    fun testStatusChange_ShouldCreateRecord() {
        runBlocking(Dispatchers.IO) {
            networkRepository.onNetworkStatusChanged(NetworkStatus.TYPE_NOT_CONNECTED)
            val lastRecord = historyDao.getLatestRecord().firstOrNull()
            assertThat(lastRecord, `is`(not(nullValue())))
        }
    }

    @Test
    fun testStatusChangeToMobileWhenNoRecords_ShouldCreateRecordFromNoConnectivityToMobile() {
        runBlocking(Dispatchers.IO) {
            networkRepository.onNetworkStatusChanged(NetworkStatus.TYPE_MOBILE)
            val lastRecord = historyDao.getLatestRecord().firstOrNull()
            assertThat(lastRecord?.fromNetwork, equalTo(HistoryRecord.NoConnectivity))
            assertThat(lastRecord?.toNetwork, equalTo(HistoryRecord.Mobile))

        }
    }

    @Test
    fun testStatusChangeToWifiWhenNoRecords_ShouldCreateRecordFromNoConnectivityToWifi() {
        runBlocking(Dispatchers.IO) {
            networkRepository.onNetworkStatusChanged(NetworkStatus.TYPE_WIFI)
            val lastRecord = historyDao.getLatestRecord().firstOrNull()
            assertThat(lastRecord?.fromNetwork, equalTo(HistoryRecord.NoConnectivity))
            assertThat(lastRecord?.toNetwork, equalTo(HistoryRecord.Wifi))
        }
    }

    @Test
    fun testStatusChangeToNoConnectivityWhenNoRecords_ShouldCreateRecordFromNoConnectivityToNoConnectivity() {
        runBlocking(Dispatchers.IO) {
            networkRepository.onNetworkStatusChanged(NetworkStatus.TYPE_NOT_CONNECTED)
            val lastRecord = historyDao.getLatestRecord().firstOrNull()
            assertThat(lastRecord?.fromNetwork, equalTo(HistoryRecord.NoConnectivity))
            assertThat(lastRecord?.toNetwork, equalTo(HistoryRecord.NoConnectivity))
        }
    }

    @Test
    fun testStatusChangeToNoConnectivityWhenHasRecords_ShouldCreateRecordFromNetworkWifi() {
        runBlocking(Dispatchers.IO) {
            networkRepository.onNetworkStatusChanged(NetworkStatus.TYPE_WIFI)
            networkRepository.onNetworkStatusChanged(NetworkStatus.TYPE_MOBILE)

            historyDao.getHistory().observeTimes(2) { list, i ->
                if (i == 2) {
                    assertThat(list.count(), equalTo(2))
                }

            }
            val lastRecord = historyDao.getLatestRecord().firstOrNull()
            assertThat(lastRecord?.fromNetwork, equalTo(HistoryRecord.Wifi))
            assertThat(lastRecord?.toNetwork, equalTo(HistoryRecord.Mobile))

        }
    }
}