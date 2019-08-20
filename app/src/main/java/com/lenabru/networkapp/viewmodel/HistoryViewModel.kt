package com.lenabru.networkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lenabru.networkapp.database.HistoryDatabase
import com.lenabru.networkapp.database.models.HistoryRecord
import com.lenabru.networkapp.repo.HistoryRepository
import java.text.SimpleDateFormat


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HistoryRepository
    val currentHistory: LiveData<List<HistoryRecord>>
    val dateFormatter: SimpleDateFormat


    init {
        val historyDAO = HistoryDatabase.getDatabase(application).historyDao()
        repository = HistoryRepository(historyDAO)
        currentHistory = repository.getHistory()
        dateFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm")
    }

}
