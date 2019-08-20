package com.lenabru.networkapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lenabru.networkapp.database.HistoryDatabase
import com.lenabru.networkapp.database.models.HistoryRecord
import com.lenabru.networkapp.repo.HistoryRepository
import kotlinx.coroutines.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.coroutines.CoroutineContext


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

@RunWith(AndroidJUnit4::class)
class HistoryRepositoryTests : CoroutineScope {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    private lateinit var db: HistoryDatabase
    private lateinit var historyRepository: HistoryRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java).build()
        val historyDao = db.historyDao()
        historyRepository = HistoryRepository(historyDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun twoHistoryRecordsCanBeEqualWithDifferentReference() {
        val timestamp = System.currentTimeMillis()
        val historyRecord1 = HistoryRecord(timestamp, HistoryRecord.Wifi, HistoryRecord.Mobile)
        val historyRecord2 = HistoryRecord(timestamp, HistoryRecord.Wifi, HistoryRecord.Mobile)

        //show that their references are different
        assertThat(historyRecord1 !== historyRecord2, equalTo(true))
        //show that their logical value is equal
        assertThat(historyRecord1, equalTo(historyRecord2))
    }

    @Test
    @Throws(Exception::class)
    fun writeHistoryRecord_ShouldAddItToTheDB() {
        val historyRecord = HistoryRecord(System.currentTimeMillis(), "No Connectivity", "WIFI")

        runBlocking(Dispatchers.IO) {
            historyRepository.insert(historyRecord)

            historyRepository.getHistory().observeOnce {
                assertThat(listOf(historyRecord), equalTo(it))
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun getLastRecord_ShouldReturnLastInsertedRecord() {
        val historyRecord1 = HistoryRecord(
            System.currentTimeMillis(),
            HistoryRecord.NoConnectivity,
            HistoryRecord.Wifi
        )
        val historyRecord2 = HistoryRecord(
            System.currentTimeMillis(),
            HistoryRecord.NoConnectivity,
            HistoryRecord.NoConnectivity
        )
        val historyRecord3 = HistoryRecord(
            System.currentTimeMillis(),
            HistoryRecord.NoConnectivity,
            HistoryRecord.Mobile
        )
        launch(Dispatchers.IO) {
            historyRepository.insert(historyRecord1)
            historyRepository.insert(historyRecord2)
            historyRepository.insert(historyRecord3)

            val lastRecord = historyRepository.getLatestRecord().firstOrNull()
            assertThat(lastRecord, equalTo(historyRecord3))

        }

    }


    @Test
    @Throws(Exception::class)
    fun inserting3Values_ShouldKeepsOrderOfInsertAndCount() {
        val historyRecord1 = HistoryRecord(
            System.currentTimeMillis(),
            HistoryRecord.NoConnectivity,
            HistoryRecord.Wifi
        )
        val historyRecord2 = HistoryRecord(
            System.currentTimeMillis(),
            HistoryRecord.NoConnectivity,
            HistoryRecord.NoConnectivity
        )
        val historyRecord3 = HistoryRecord(
            System.currentTimeMillis(),
            HistoryRecord.NoConnectivity,
            HistoryRecord.Mobile
        )
        runBlocking(Dispatchers.IO) {
            historyRepository.insert(historyRecord1)
            historyRepository.insert(historyRecord2)
            historyRepository.insert(historyRecord3)
            historyRepository.getHistory().observeTimes(3) { list, i ->
                //this should get invoked 3 times
                if (i == 3) {
                    assertThat(list.count(), equalTo(3))
                    assertThat(
                        listOf(historyRecord1, historyRecord2, historyRecord3),
                        equalTo(list)
                    )
                }
            }

        }

    }

    @Test
    fun canUpdateAValueInTheDB() {
        val historyRecord1 = HistoryRecord(
            System.currentTimeMillis(),
            HistoryRecord.NoConnectivity,
            HistoryRecord.Wifi
        )

        runBlocking(Dispatchers.IO) {
            historyRepository.insert(historyRecord1)

            val lastRecord = historyRepository.getLatestRecord().firstOrNull()
            assertThat(lastRecord?.sentToServer, `is`(false))
            historyRecord1.sentToServer = true

            historyRepository.update(historyRecord1)

            val lastRecord1 = historyRepository.getLatestRecord().firstOrNull()
            assertThat(lastRecord1, equalTo(historyRecord1))
            assertThat(lastRecord?.sentToServer, `is`(false))
            assertThat(lastRecord1?.sentToServer, `is`(true))

        }
    }

}