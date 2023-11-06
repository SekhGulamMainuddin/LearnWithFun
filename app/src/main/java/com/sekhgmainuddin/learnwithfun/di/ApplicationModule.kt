package com.sekhgmainuddin.learnwithfun.di

import android.content.Context
import androidx.room.Room
import com.sekhgmainuddin.learnwithfun.common.helper.PrefsHelper
import com.sekhgmainuddin.learnwithfun.data.db.LearnWithFunDb
import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.data.repository.LoginSignUpRepositoryImpl
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideRetrofit(): LearnWithFunApi {
        return Retrofit.Builder().baseUrl("http://192.168.170.37:8000")
            .addConverterFactory(GsonConverterFactory.create())
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

    @Singleton
    @Provides
    fun provideLoginSignUpRepository(learnWithFunApi: LearnWithFunApi) : LoginSignUpRepository =
        LoginSignUpRepositoryImpl(learnWithFunApi)

}