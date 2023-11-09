package com.sekhgmainuddin.learnwithfun.di

import com.google.firebase.storage.StorageReference
import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.data.repository.HomeRepositoryImp
import com.sekhgmainuddin.learnwithfun.data.repository.LoginSignUpRepositoryImpl
import com.sekhgmainuddin.learnwithfun.data.repository.UploadFileRepositoryImpl
import com.sekhgmainuddin.learnwithfun.domain.repository.HomeRepository
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import com.sekhgmainuddin.learnwithfun.domain.repository.UploadFileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginSignUpRepository(learnWithFunApi: LearnWithFunApi): LoginSignUpRepository =
        LoginSignUpRepositoryImpl(learnWithFunApi)

    @Singleton
    @Provides
    fun uploadFileRepository(storageReference: StorageReference): UploadFileRepository =
        UploadFileRepositoryImpl(storageReference)

    @Singleton
    @Provides
    fun provideHomeRepository(learnWithFunApi: LearnWithFunApi): HomeRepository =
        HomeRepositoryImp(learnWithFunApi)

}