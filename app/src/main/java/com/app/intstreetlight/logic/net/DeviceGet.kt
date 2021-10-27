package com.app.intstreetlight.logic.net

import com.app.intstreetlight.StreetLightApplication.Companion.deviceList
import com.app.intstreetlight.getClient
import com.app.intstreetlight.logic.model.Device
import com.huaweicloud.sdk.iotda.v5.model.ListDevicesRequest
import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceShadowRequest

object DeviceGet {

    fun getDevices() {
        val request =  ListDevicesRequest()
        val list = getClient().listDevices(request).devices as ArrayList<QueryDeviceSimplify>
        list.forEach {
            deviceList.add(Device(it,null))
        }
    }

    fun getProperties(devices: ArrayList<Device>){
        val request = ShowDeviceShadowRequest()
        devices.forEach {
            request.withDeviceId(it.deviceObj.deviceId)
            val shadow = getClient().showDeviceShadow(request).shadow
            if (shadow.size == 0) {
                it.properties = null
            }else{
                it.properties = shadow[0].reported.properties as HashMap<*, *>
            }
        }

    }

}