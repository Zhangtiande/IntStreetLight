package com.app.intstreetlight

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.app.intstreetlight.StreetLightApplication.Companion.client
import com.app.intstreetlight.StreetLightApplication.Companion.context
import com.app.intstreetlight.logic.dao.DeviceSQLHelper
import com.app.intstreetlight.logic.net.Client
import com.huaweicloud.sdk.iotda.v5.IoTDAClient

class StreetLightApplication: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        var client: IoTDAClient? = null
        var index = 1
        val helper: SQLiteOpenHelper by lazy {
            DeviceSQLHelper(context, "Device.db", 2)
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}

fun String.showToast() {
    Toast.makeText(context,this, Toast.LENGTH_SHORT).show()
}


fun getClient(): IoTDAClient {
    return if (client == null) {
        client = Client.iotClient()
        client as IoTDAClient
    } else client as IoTDAClient
}