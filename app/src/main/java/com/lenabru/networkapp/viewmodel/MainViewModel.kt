package com.lenabru.networkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.lenabru.networkapp.api.Api
import com.lenabru.networkapp.database.HistoryDatabase
import com.lenabru.networkapp.database.models.HistoryRecord
import com.lenabru.networkapp.network.NetworkChangeCallback
import com.lenabru.networkapp.network.NetworkStatusRepository
import com.lenabru.networkapp.repo.HistoryRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


/* 
 * Created by Lena Brusilovski on 2019-08-20
 */

class MainViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val dao = HistoryDatabase.getDatabase(application.applicationContext).historyDao()
    private val networkChangeListenerStatus = MutableLiveData(false)
    private val showLogsStatus = MutableLiveData(false)
    private val lifeCycleOwner = LifeCycleOwnerImpl()

    private val networkStatusRepository: NetworkStatusRepository by lazy {
        val apiService = Api.service
        NetworkStatusRepository(dao, apiService)
    }

    private val networkStatusCallback =
        NetworkChangeCallback(application.applicationContext, networkStatusRepository)

    private val historyRepository: HistoryRepository = HistoryRepository(dao)

    init {
        historyRepository.getUnsentRecords().observe(lifeCycleOwner, Observer {
            GlobalScope.launch(Dispatchers.IO) {
                withContext(coroutineContext) {
                    if (it.count() > 0) {
                        networkStatusRepository.sendServerUpdates(it)
                    }
                }
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
        lifeCycleOwner.clear()
    }

    fun onStartButtonClicked() {
        val isEnabled = networkChangeListenerStatus.value ?: false
        if (isEnabled) {
            networkStatusCallback.disable()
        } else {
            networkStatusCallback.enable()
        }
        networkChangeListenerStatus.postValue(!isEnabled)

    }

    fun onLogsButtonClicked() {
        showLogsStatus.postValue(true)
    }

    fun onBack() {
        showLogsStatus.value?.let {
            if (it) {
                showLogsStatus.value = false
            }
        }
    }

    fun networkChangeListenerEnabled(): LiveData<Boolean> = networkChangeListenerStatus
    fun showLogs(): LiveData<Boolean> = showLogsStatus
    fun lastRecordUpdate(): LiveData<List<HistoryRecord>> = historyRepository.getLiveLatestRecord()

}