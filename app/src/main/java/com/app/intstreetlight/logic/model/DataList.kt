package com.app.intstreetlight.logic.model

import com.google.gson.annotations.SerializedName


data class DataList(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("deviceId")
    var deviceId: String,
    @SerializedName("ret")
    var ret: String
)

data class Data(
    @SerializedName("AmbientLight")
    var ambientLight: Int,
    @SerializedName("AutomaticDimming")
    var automaticDimming: Int,
    @SerializedName("Fog")
    var fog: Int,
    @SerializedName("LightIntensity")
    var lightIntensity: Int,
    @SerializedName("Raindrops")
    var raindrops: Int,
    @SerializedName("timeStamp")
    var timeStamp: Long
)