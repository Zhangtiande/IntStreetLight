package com.app.intstreetlight.logic.net

import com.app.intstreetlight.logic.model.DataList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DataListService {

    @GET("properties.api")
    fun getRealtimeWeather(@Query("deviceId") id: String):
            Call<DataList>
}