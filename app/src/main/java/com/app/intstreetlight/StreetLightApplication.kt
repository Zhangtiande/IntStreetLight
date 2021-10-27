package com.app.intstreetlight

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.app.intstreetlight.StreetLightApplication.Companion.client
import com.app.intstreetlight.StreetLightApplication.Companion.context
import com.app.intstreetlight.logic.dao.ApplicationInfoDao.isInfoSaved
import com.app.intstreetlight.logic.dao.DeviceSQLHelper
import com.app.intstreetlight.logic.model.Device
import com.app.intstreetlight.logic.net.Client
import com.huaweicloud.sdk.iotda.v5.IoTDAClient

class StreetLightApplication: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        var deviceList: ArrayList<Device> = ArrayList()
        var client: IoTDAClient? = null
        var isFirstOpen:Boolean = false
        var index = 1
        lateinit var helper: SQLiteOpenHelper
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        if (!isInfoSaved("firstOpen")) {
            isFirstOpen = true
        }
        helper = DeviceSQLHelper(context,"Device.db",1)
    }
}

fun String.showToast() {
    Toast.makeText(context,this, Toast.LENGTH_SHORT).show()
}

fun cvOf(vararg pairs: Pair<String, Any?>) = ContentValues().apply {
    for (pair in pairs) {
        val key = pair.first
        when (val value = pair.second) {
            is Int -> put(key, value)
            is Long -> put(key, value)
            is Short -> put(key, value)
            is Float -> put(key, value)
            is Double -> put(key, value)
            is Boolean -> put(key, value)
            is String -> put(key, value)
            is Byte -> put(key, value)
            is ByteArray -> put(key, value)
            null -> putNull(key)
        }
    }
}

fun getClient(): IoTDAClient {
    if (client == null) {
        client = Client.iotClient()
        return client as IoTDAClient
    }else return client as IoTDAClient
}