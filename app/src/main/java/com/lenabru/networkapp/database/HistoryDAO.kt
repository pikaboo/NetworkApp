package com.lenabru.networkapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lenabru.networkapp.database.models.HistoryRecord


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */


@Dao
interface HistoryDAO {

    @Query("SELECT * from historytable ORDER BY timestamp ASC")
    fun getHistory(): LiveData<List<HistoryRecord>>

    @Query("SELECT * from historytable where timestamp = (select MAX(timestamp) from historytable)")
    suspend fun getLatestRecord(): List<HistoryRecord>

    @Query("SELECT * from historytable where timestamp = (select MAX(timestamp) from historytable)")
    fun getLiveLatestRecord(): LiveData<List<HistoryRecord>>

    @Query("SELECT * from historytable where sentToServer = 0 ORDER BY timestamp ASC")
    fun getUnsentRecords(): LiveData<List<HistoryRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyRecord: HistoryRecord)

    @Update
    suspend fun update(historyRecord: HistoryRecord)

}