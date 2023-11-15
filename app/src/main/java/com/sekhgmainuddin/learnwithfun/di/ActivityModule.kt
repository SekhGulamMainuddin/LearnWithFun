package com.sekhgmainuddin.learnwithfun.di

import android.app.Dialog
import android.content.Context
import com.sekhgmainuddin.learnwithfun.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @ActivityScoped
    @Named("progressDialog")
    fun providesProgressDialog1(@ActivityContext context: Context) =
        Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen).apply {
            setContentView(R.layout.progress_dialog)
            setCancelable(false)
        }


}