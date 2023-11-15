package com.sekhgmainuddin.learnwithfun.common.helper

import android.content.Context
import android.content.SharedPreferences
import com.sekhgmainuddin.learnwithfun.common.Constants.LEARN_WITH_FUN_TOKEN
import com.sekhgmainuddin.learnwithfun.data.remote.dto.UserDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PrefsHelper @Inject constructor(@ApplicationContext context: Context) {

    var sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences("learn_with_fun_sp", Context.MODE_PRIVATE)
    }

    inline fun <reified T> putValue(key: String, value: T) {
        sharedPrefs.edit().apply {
            when (T::class) {
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

    inline fun <reified T> getValue(key: String, defaultValue: T? = null): T? {
        sharedPrefs.apply {
            return try {
                when (T::class) {
                    Integer::class -> {
                        getInt(key, defaultValue as Int)
                    }

                    Long::class -> {
                        getLong(key, defaultValue as Long)
                    }

                    String::class -> {
                        getString(key, defaultValue as String?)
                    }

                    Boolean::class -> {
                        getBoolean(key, defaultValue as Boolean)
                    }
                    UserDto::class -> {
                        Json.decodeFromString<UserDto>(getString(key, "")!!)
                    }

                    else -> throw IllegalStateException("Unsupported type")
                }
            } catch (e: Exception) {
                null
            } as T?
        }
    }

    fun updateToken(token: String) = putValue(LEARN_WITH_FUN_TOKEN, token)

    fun clearToken() = putValue(LEARN_WITH_FUN_TOKEN, "")

    fun getToken() = getValue(LEARN_WITH_FUN_TOKEN, "") ?: ""

}