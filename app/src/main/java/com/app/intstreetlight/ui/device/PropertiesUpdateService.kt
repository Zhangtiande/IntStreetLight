package com.app.intstreetlight.ui.device

import android.app.IntentService
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import com.app.intstreetlight.StreetLightApplication
import com.app.intstreetlight.cvOf
import com.app.intstreetlight.logic.net.DeviceGet.getProperties


class PropertiesUpdateService : IntentService("PropertiesUpdateService") {
    private lateinit var db: SQLiteDatabase

    override fun onHandleIntent(intent: Intent?) {
        while (true) {
            getProperties()
            db = StreetLightApplication.helper.writableDatabase
            db.beginTransaction()
            try {
                StreetLightApplication.deviceList.forEach {
                    if (it.properties != null) {
                        val content = cvOf(
                            "timeStamp" to it.eventTime,
                            "deviceId" to it.deviceObj.deviceId,
                            "ambientLight" to it.properties!!["AmbientLight"],
                            "lightIntensity" to it.properties!!["LightIntensity"],
                            "raindrops" to it.properties!!["Raindrops"],
                            "automaticDimming" to it.properties!!["AutomaticDimming"],
                            "fog" to it.properties!!["Fog"]
                        )
                        db.insert("properties", null, content)
                    }
                }
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                db.endTransaction()
            }
            db.close()

            Thread.sleep(20000)
        }

    }


}