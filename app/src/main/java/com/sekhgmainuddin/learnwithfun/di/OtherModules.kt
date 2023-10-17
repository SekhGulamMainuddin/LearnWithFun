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

@Module
@InstallIn(ActivityComponent::class)
object OtherModules {

    @Provides
    @ActivityScoped
    fun providesProgressDialog(@ActivityContext context: Context) = Dialog(context).apply {
        setContentView(R.layout.progress_dialog)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


}