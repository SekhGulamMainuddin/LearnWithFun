package com.sekhgmainuddin.learnwithfun.common.helper

import android.content.Context
import android.content.SharedPreferences

class PrefsHelper(context: Context) {

    var sharedPrefs : SharedPreferences
    init {
        sharedPrefs = context.getSharedPreferences("learn_with_fun_sp", Context.MODE_PRIVATE)
    }

    inline fun <reified T> putValue(key: String, value: T) {
        sharedPrefs.edit().apply {
            when(T::class){
                Integer::class -> {
                    putInt(key, value as Int)
                }
                Long::class -> {
                    putLong(key, value as Long)
                }
                String::class -> {
                    putString(key, value as String)
                }
                Boolean::class -> {
                    putBoolean(key, value as Boolean)
                }
                else -> throw IllegalStateException("Unsupported type")
            }
            apply()
        }
    }

}