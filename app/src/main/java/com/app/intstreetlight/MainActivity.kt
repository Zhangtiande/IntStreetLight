package com.app.intstreetlight

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.intstreetlight.databinding.ActivityMainBinding
import com.app.intstreetlight.logic.Repository
import com.app.intstreetlight.logic.model.MainViewModel
import com.app.intstreetlight.ui.device.DeviceAdapter
import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: DeviceAdapter
    private lateinit var binding: ActivityMainBinding

    private val loadSuccess = 1
    private val loadGone = 2

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                loadSuccess -> binding.loadingView.showSuccess()
                loadGone -> binding.loadingView.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = DeviceAdapter(this, ArrayList())
        binding.recyclerView.adapter = adapter
        val devicesObserver = Observer<ArrayList<QueryDeviceSimplify>> {
            adapter = DeviceAdapter(applicationContext, it)
            binding.recyclerView.adapter = adapter
            "刷新成功！".showToast()
        }
        viewModel.devices.observe(this, devicesObserver)
        binding.refresh.setOnClickListener {
            thread {
                viewModel.setDevices(Repository.getDevices())
            }
        }
        thread {
            viewModel.setDevices(Repository.getDevices())
            val msg = Message()
            msg.what = loadGone
            handler.sendMessageDelayed(msg, 500)
        }
    }

}