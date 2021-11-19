package com.app.intstreetlight.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.app.intstreetlight.logic.Repository
import java.text.SimpleDateFormat
import java.util.*

class DeviceViewModel : ViewModel() {

    private val _devices = MutableLiveData<ArrayList<Device>>()

    private val _index = MutableLiveData<Int>()


    private val _deviceObj = MutableLiveData<Device>()

    val currentDeviceName: LiveData<String> = Transformations.map(_deviceObj) {
        it.deviceObj.deviceName
    }

    val currentDeviceStatus: LiveData<String> = Transformations.map(_deviceObj) {
        it.deviceObj.status ?: "null"
    }

    val currentDeviceProperty: LiveData<HashMap<*, *>> = Transformations.map(_deviceObj) {
        it.properties
    }

    val currentUpdateTime: LiveData<String> = Transformations.map(_deviceObj) {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        format.format(it.eventTime)
    }

    val currentDataList = Transformations.switchMap(currentUpdateTime) {
        Repository.getDataList(_devices.value!![_index.value!!].deviceObj.deviceId)
    }


    fun setIndex(i: Int) {
        _index.postValue(i)
    }

    fun setDeviceObj(d: Device) {
        _deviceObj.postValue(d)
    }

    fun setList(list: ArrayList<Device>) {
        _devices.postValue(list)
    }

}