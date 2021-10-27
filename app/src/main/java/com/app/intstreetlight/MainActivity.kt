package com.app.intstreetlight

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.intstreetlight.StreetLightApplication.Companion.deviceList
import com.app.intstreetlight.logic.dao.ApplicationInfoDao
import com.app.intstreetlight.logic.net.DeviceGet
import com.app.intstreetlight.ui.device.DeviceAdapter
import com.app.intstreetlight.ui.load.LoadingView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var loadingView: LoadingView
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerView)
        loadingView = findViewById(R.id.loadingView)
        floatButton = findViewById(R.id.refresh)
        recyclerView.layoutManager = layoutManager
        loadingView.showLoading()
        loadingView.setText("正在加载")
        floatButton.setOnClickListener {
            thread {
                runBlocking {
                    DeviceGet.getDevices()
                }
                runOnUiThread {
                    val adapter = DeviceAdapter(this, deviceList)
                    recyclerView.adapter = adapter
                    "刷新成功！".showToast()
                }
            }
        }
        thread {
            runBlocking {
                DeviceGet.getDevices()
                DeviceGet.getProperties(deviceList)
            }
            runOnUiThread {
                if (StreetLightApplication.isFirstOpen) {
                    val db = StreetLightApplication.helper.writableDatabase
                    deviceList.forEach {
                        val contentValues = cvOf(
                            "deviceId" to it.deviceObj.deviceId,
                            "deviceName" to it.deviceObj.deviceName,
                            "deviceDes" to it.deviceObj.description
                        )
                        db.insert("device", null, contentValues)
                    }
                    ApplicationInfoDao.saveInfo("firstOpen" to "true")
                    db.close()
                }
                loadingView.showSuccess()
                loadingView.setText("加载成功")
            }
            runBlocking {
                delay(800)
            }
            runOnUiThread {
                loadingView.visibility = View.GONE
                val adapter = DeviceAdapter(this, deviceList)
                recyclerView.adapter = adapter
            }
        }
    }
}