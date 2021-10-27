package com.app.intstreetlight.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.app.intstreetlight.StreetLightApplication

object ApplicationInfoDao {

    fun saveInfo(info: Pair<String,String>) {
        sharedPreferences().edit {
            putString(info.first, info.second)
        }
    }

    private fun sharedPreferences() = StreetLightApplication.context.getSharedPreferences(
        " app_info",
        Context.MODE_PRIVATE
    )

    fun getSavedInfo(key: String): String? {
        return sharedPreferences().getString(key, "")
    }

    fun isInfoSaved(key: String) = sharedPreferences().contains(key)
}