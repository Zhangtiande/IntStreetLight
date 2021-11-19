package com.app.intstreetlight.logic.model

import com.huaweicloud.sdk.iotda.v5.model.QueryDeviceSimplify
import java.util.*

data class Device(
    var deviceObj: QueryDeviceSimplify,
    var properties: HashMap<*, *>?,
    var eventTime: Long?,
)


data class DatList(
    val ambientLight: ArrayDeque<Int>,
    val lightIntensity: ArrayDeque<Int>,
    val raindrops: ArrayDeque<Int>,
    val automaticDimming: ArrayDeque<Int>,
    val fog: ArrayDeque<Int>,
    val time: ArrayDeque<Long>
)