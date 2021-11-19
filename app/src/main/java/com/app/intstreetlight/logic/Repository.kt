package com.app.intstreetlight.logic

import androidx.lifecycle.liveData
import com.app.intstreetlight.getClient
import com.app.intstreetlight.logic.model.Device
import com.app.intstreetlight.logic.net.DataListNetWork
import com.huaweicloud.sdk.iotda.v5.model.ListDevicesRequest
import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceRequest
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceShadowRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.*

object Repository {

    fun getDevices() = with(ListDevicesRequest()) {
        getClient().listDevices(this).devices as ArrayList<QueryDeviceSimplify>
    }


    fun getProperties(id: String, t: Long): Device {
        val request = ShowDeviceShadowRequest()
        var properties: HashMap<*, *>? = null
        var time: Long? = null
        val req = ShowDeviceRequest()
        req.withDeviceId(id)
        request.withDeviceId(id)
        val resp = getClient().showDevice(req)
        val shadow = getClient().showDeviceShadow(request).shadow
        if (shadow.size != 0) {
            val eventTime = shadow[0].reported.eventTime
            val format = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.CHINA)
            time = format.parse(eventTime)!!.time
            if (t != time) {
                properties = shadow[0].reported.properties as HashMap<*, *>?
            }
        }
        val obj = QueryDeviceSimplify()
        obj.deviceName = resp.deviceName
        obj.status = resp.status
        obj.deviceId = resp.deviceId
        return Device(obj, properties, time)

    }

    fun getDataList(id: String) = fire {
        coroutineScope {
            val dataListWait = async {
                DataListNetWork.getDataList(id)
            }
            val dataList = dataListWait.await()
            if (dataList.ret == "0") {

                Result.success(dataList)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is $dataList"
                    )
                )
            }
        }
    }


    private fun <T> fire(block: suspend () -> Result<T>) =
        liveData {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }

}