package com.app.intstreetlight.logic.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DeviceSQLHelper(ctx:Context,n: String, v: Int): SQLiteOpenHelper(ctx,n,null,v) {

    private val queryDevice = "create table device (" +
            "id integer primary key autoincrement," +
            "deviceId text," +
            "deviceName text," +
            "deviceDes text)"

    private val propertiesQuery = "create table properties (" +
            "timeStamp integer primary key ," +
            "deviceId text," +
            "ambientLight integer," +
            "lightIntensity integer," +
            "raindrops integer," +
            "automaticDimming integer," +
            "fog integer," +
            "foreign key(deviceId) references device(deviceId))"


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(queryDevice)
        db.execSQL(propertiesQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 1) {
            db!!.execSQL(propertiesQuery)
        }
    }


}

fun cvOf(vararg pairs: Pair<String, Any?>) = ContentValues().apply {
    for (pair in pairs) {
        val key = pair.first
        when (val value = pair.second) {
            is Int -> put(key, value)
            is Long -> put(key, value)
            is Short -> put(key, value)
            is Float -> put(key, value)
            is Double -> put(key, value)
            is Boolean -> put(key, value)
            is String -> put(key, value)
            is Byte -> put(key, value)
            is ByteArray -> put(key, value)
            null -> putNull(key)
        }
    }
}