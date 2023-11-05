package com.sekhgmainuddin.learnwithfun.common.helper

sealed class NetworkResult<T>(var data: T? = null, var message: String? = null, var statusCode: Int? = null) {
    class Success<T>(data: T, statusCode: Int? = null) : NetworkResult<T>(data,null, statusCode)
    class Error<T>(message: String?, data: T? = null, statusCode: Int? = null) : NetworkResult<T>(data, message, statusCode)
    class Loading<T> : NetworkResult<T>()
}