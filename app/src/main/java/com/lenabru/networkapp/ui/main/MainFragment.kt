package com.lenabru.networkapp.ui.history.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lenabru.networkapp.R
import com.lenabru.networkapp.ui.history.HistoryFragment
import com.lenabru.networkapp.viewmodel.LifeCycleOwnerImpl
import com.lenabru.networkapp.viewmodel.MainViewModel


/* 
 * Created by Lena Brusilovski on 2019-08-20
 */

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private val lifeCycleOwner = LifeCycleOwnerImpl()
    private val historyFragmentTag = "HistoryFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.layout_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val enableButton: Button = view.findViewById(R.id.start)
        val logsButton: Button = view.findViewById(R.id.logs)
        val statusLine: TextView = view.findViewById(R.id.statusLine)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.networkChangeListenerEnabled().observe(lifeCycleOwner, Observer { enabled ->
            enableButton.setText(
                when (enabled) {
                    true -> R.string.disable
                    false -> R.string.enable
                }
            )
        })
        viewModel.lastRecordUpdate().observe(lifeCycleOwner, Observer {
            it.firstOrNull()?.let { record ->
                statusLine.text = record.toNetwork
            }
        })
        viewModel.showLogs().observe(lifeCycleOwner, Observer {
            //potential problem overclicking the button too fast
            if (it) {
                fragmentManager?.let { fm ->
                    if (fm.findFragmentByTag(historyFragmentTag) != null) {
                        //fragment already in view
                        return@Observer
                    }
                    fm.beginTransaction()
                        .replace(R.id.logListContainr, HistoryFragment(), historyFragmentTag)
                        .addToBackStack(null)
                        .commit()
                }
            }
        })
        enableButton.setOnClickListener {
            //potential problem overclicking the button too fast
            viewModel.onStartButtonClicked()
        }

        logsButton.setOnClickListener {
            //potential problem overclicking the button too fast
            viewModel.onLogsButtonClicked()
        }

        fragmentManager?.addOnBackStackChangedListener {
            if (fragmentManager?.backStackEntryCount == 0) {
                viewModel.onBack()
            }
        }
    }

}