package com.app.intstreetlight.ui.device

import android.app.IntentService
import android.content.Intent
import android.database.sqlite.SQLiteDatabase


class PropertiesUpdateService : IntentService("PropertiesUpdateService") {
    private lateinit var db: SQLiteDatabase

    override fun onHandleIntent(intent: Intent?) {
        while (true) {


            Thread.sleep(20000)
        }

    }


}