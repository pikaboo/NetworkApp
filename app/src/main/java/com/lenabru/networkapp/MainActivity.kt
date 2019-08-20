package com.lenabru.networkapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lenabru.networkapp.database.models.HistoryRecord
import com.lenabru.networkapp.ui.history.HistoryFragment
import com.lenabru.networkapp.network.NetworkChangeCallback
import com.lenabru.networkapp.ui.history.main.MainFragment
import com.lenabru.networkapp.viewmodel.HistoryViewModel
import com.lenabru.networkapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = MainFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .commitAllowingStateLoss()

    }

}
