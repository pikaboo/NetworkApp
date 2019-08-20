package com.lenabru.networkapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lenabru.networkapp.database.models.HistoryRecord


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */
@Database(entities = [HistoryRecord::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDAO

    companion object {
        @Volatile
        private var database: HistoryDatabase? = null

        fun getDatabase(context: Context): HistoryDatabase {
            val tempInstance = database
            tempInstance?.let {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "HistoryDatabase"
                ).build()
                database = instance
                return instance
            }
        }
    }
}