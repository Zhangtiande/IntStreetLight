package com.app.intstreetlight

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.intstreetlight.logic.model.MainViewModel
import com.app.intstreetlight.logic.net.DeviceGet
import com.app.intstreetlight.ui.device.DeviceAdapter
import com.app.intstreetlight.ui.load.LoadingView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var loadingView: LoadingView
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatButton: FloatingActionButton
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerView)
        loadingView = findViewById(R.id.loadingView)
        floatButton = findViewById(R.id.refresh)
        recyclerView.layoutManager = layoutManager
        adapter = DeviceAdapter(this, ArrayList())
        recyclerView.adapter = adapter
        val devicesObserver = Observer<ArrayList<QueryDeviceSimplify>> {
            adapter = DeviceAdapter(applicationContext, it)
            recyclerView.adapter = adapter
            "刷新成功！".showToast()
        }
        viewModel.devices.observe(this, devicesObserver)
        loadingView.showLoading()
        loadingView.setText("正在加载")
        floatButton.setOnClickListener {
            thread {
                viewModel.setDevices(DeviceGet.getDevices())
            }
        }
        thread {
            viewModel.setDevices(DeviceGet.getDevices())
            loadingView.showSuccess()
            loadingView.setText("加载成功")
            thread {
                runBlocking {
                    delay(600)
                }
                runOnUiThread {
                    loadingView.visibility = View.GONE
                }
            }
        }
    }
}