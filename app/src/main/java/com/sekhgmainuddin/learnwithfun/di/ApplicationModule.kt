package com.sekhgmainuddin.learnwithfun.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sekhgmainuddin.learnwithfun.common.TEMP.BASE_URL
import com.sekhgmainuddin.learnwithfun.common.helper.PrefsHelper
import com.sekhgmainuddin.learnwithfun.data.db.LearnWithFunDb
import com.sekhgmainuddin.learnwithfun.data.remote.ApiInterceptor
import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): LearnWithFunApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
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
    fun provideTimeShareDao(learnWithFunDb: LearnWithFunDb) = learnWithFunDb.getDao()

    @Singleton
    @Provides
    fun providerOkHttpClient(interceptor: ApiInterceptor) =
        OkHttpClient().newBuilder().addInterceptor(interceptor).build()

    @Singleton
    @Provides
    fun provideFirebaseStorage() = Firebase.storage.reference

    @Singleton
    @Provides
    fun providePrefsHelper(@ApplicationContext context: Context) = PrefsHelper(context)

}