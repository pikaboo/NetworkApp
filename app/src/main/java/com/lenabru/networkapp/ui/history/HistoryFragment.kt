package com.lenabru.networkapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.lenabru.networkapp.R
import com.lenabru.networkapp.viewmodel.HistoryViewModel


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryRecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.layout_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list)
        adapter = HistoryRecordAdapter(viewModel.dateFormatter)
        recyclerView.adapter = adapter
        viewModel.currentHistory.observe(this, Observer {
            adapter.setHistory(it)
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        })
    }

}