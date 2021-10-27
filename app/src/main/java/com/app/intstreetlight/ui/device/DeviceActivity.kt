package com.app.intstreetlight.ui.device

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.intstreetlight.R

class DeviceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.deviceDetail, DeviceFragment.newInstance())
        transaction.commit()
    }


}