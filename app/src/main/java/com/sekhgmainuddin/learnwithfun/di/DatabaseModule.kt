package com.sekhgmainuddin.learnwithfun.di


import android.content.Context
import androidx.room.Room
import com.sekhgmainuddin.learnwithfun.data.db.LearnWithFunDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideTimeShareDatabase(@ApplicationContext context: Context): LearnWithFunDb {
        return synchronized(this){
            Room.databaseBuilder(
                context.applicationContext,
                LearnWithFunDb::class.java,
                "learn_with_fun_db"
            ).fallbackToDestructiveMigration().build()
        }
    }

}