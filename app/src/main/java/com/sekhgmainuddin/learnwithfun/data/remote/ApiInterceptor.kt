package com.sekhgmainuddin.learnwithfun.data.remote

import com.sekhgmainuddin.learnwithfun.common.helper.PrefsHelper
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiInterceptor @Inject constructor(private val prefsHelper: PrefsHelper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("x-auth-token", prefsHelper.getToken())
                .build()
        )
    }
}