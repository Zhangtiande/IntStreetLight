package com.app.intstreetlight.logic.net

import com.app.intstreetlight.StreetLightApplication.Companion.deviceList
import com.app.intstreetlight.StreetLightApplication.Companion.helper
import com.app.intstreetlight.getClient
import com.app.intstreetlight.logic.model.DatList
import com.huaweicloud.sdk.iotda.v5.model.ListDevicesRequest
import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceShadowRequest
import java.text.SimpleDateFormat
import java.util.*

object DeviceGet {

     fun getDevices(): ArrayList<QueryDeviceSimplify> {
         val request = ListDevicesRequest()
         return getClient().listDevices(request).devices as ArrayList<QueryDeviceSimplify>
     }

    fun getProperties() {
        val request = ShowDeviceShadowRequest()
        deviceList.forEach {
            request.withDeviceId(it.deviceObj.deviceId)
            val shadow = getClient().showDeviceShadow(request).shadow
            if (shadow.size != 0) {
                val eventTime = shadow[0].reported.eventTime
                val format = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.CHINA)
                val time = format.parse(eventTime)!!.time
                if (it.eventTime != time) {
                    it.eventTime = time
                    it.properties = shadow[0].reported.properties as HashMap<*, *>
//                    it.datList!!.time.addFirst(time)
//                    it.datList!!.time.removeLast()
//                    it.datList!!.ambientLight.addFirst(it.properties!!["AmbientLight"] as Int)
//                    it.datList!!.ambientLight.removeLast()
//                    it.datList!!.lightIntensity.addFirst(it.properties!!["LightIntensity"] as Int)
//                    it.datList!!.lightIntensity.removeLast()
//                    it.datList!!.raindrops.addFirst(it.properties!!["Raindrops"] as Int)
//                    it.datList!!.raindrops.removeLast()
//                    it.datList!!.automaticDimming.addFirst(it.properties!!["AutomaticDimming"] as Int)
//                    it.datList!!.automaticDimming.removeLast()
//                    it.datList!!.fog.addFirst(it.properties!!["Fog"] as Int)
//                    it.datList!!.fog.removeLast()
                }
            }
        }

    }

    fun sqliteDataGet() {
        val db = helper.writableDatabase
        deviceList.forEach {
            it.datList = DatList(
                ArrayDeque(), ArrayDeque(), ArrayDeque(), ArrayDeque(),
                ArrayDeque(), ArrayDeque()
            )
            val cursor = db.query(
                "properties", null, "deviceId=?", arrayOf(it.deviceObj.deviceId),
                null, null, "timeStamp DESC", "8"
            )
            if (cursor.moveToNext()) {
                do {
                    it.datList!!.time.addLast(cursor.getLong(0))
                    it.datList!!.ambientLight.addLast(cursor.getInt(2))
                    it.datList!!.lightIntensity.addLast(cursor.getInt(3))
                    it.datList!!.raindrops.addLast(cursor.getInt(4))
                    it.datList!!.automaticDimming.addLast(cursor.getInt(5))
                    it.datList!!.fog.addLast(cursor.getInt(6))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }

}