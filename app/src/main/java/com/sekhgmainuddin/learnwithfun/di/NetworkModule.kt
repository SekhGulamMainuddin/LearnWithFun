package com.sekhgmainuddin.learnwithfun.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): LearnWithFunApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder().baseUrl("localhost:8000")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(LearnWithFunApi::class.java)
    }

}