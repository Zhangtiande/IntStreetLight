package com.app.intstreetlight.logic.net

import com.app.intstreetlight.StreetLightApplication.Companion.deviceList
import com.app.intstreetlight.getClient
import com.app.intstreetlight.logic.model.Device
import com.huaweicloud.sdk.iotda.v5.model.ListDevicesRequest
import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceShadowRequest
import java.text.SimpleDateFormat
import java.util.*

object DeviceGet {

    fun getDevices() {
        val request = ListDevicesRequest()
        val list = getClient().listDevices(request).devices as ArrayList<QueryDeviceSimplify>
        list.forEach {
            deviceList.add(Device(it, null, null))
        }
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
                }
            }
        }

    }

}