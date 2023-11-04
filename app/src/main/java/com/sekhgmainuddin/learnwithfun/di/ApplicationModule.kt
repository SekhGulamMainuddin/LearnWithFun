package com.sekhgmainuddin.learnwithfun.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sekhgmainuddin.learnwithfun.common.helper.PrefsHelper
import com.sekhgmainuddin.learnwithfun.data.db.LearnWithFunDb
import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideRetrofit(): LearnWithFunApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder().baseUrl("localhost:8000")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(LearnWithFunApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTimeShareDatabase(@ApplicationContext context: Context): LearnWithFunDb {
        return synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                LearnWithFunDb::class.java,
                "learn_with_fun_db"
            ).fallbackToDestructiveMigration().build()
        }
    }

    @Singleton
    @Provides
    fun providePrefsHelper(@ApplicationContext context: Context) = PrefsHelper(context)

}