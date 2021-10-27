package com.app.intstreetlight.logic.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DeviceSQLHelper(ctx:Context,n: String, v: Int): SQLiteOpenHelper(ctx,n,null,v) {

    private val queryDevice = "create table device (" +
            "id integer primary key autoincrement," +
            "deviceId text," +
            "deviceName text," +
            "deviceDes text)"


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(queryDevice)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


}