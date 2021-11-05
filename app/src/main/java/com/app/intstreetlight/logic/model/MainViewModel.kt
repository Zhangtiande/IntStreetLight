package com.app.intstreetlight.logic.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify

class MainViewModel : ViewModel() {


    val devices: LiveData<ArrayList<QueryDeviceSimplify>>
        get() = _devices

    private val _devices = MutableLiveData<ArrayList<QueryDeviceSimplify>>()


}