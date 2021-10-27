package com.app.intstreetlight.logic.model

import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify

data class Device(
    val deviceObj: QueryDeviceSimplify,
    var properties: HashMap<*, *>?
)