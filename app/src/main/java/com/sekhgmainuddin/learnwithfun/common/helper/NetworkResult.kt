package com.sekhgmainuddin.learnwithfun.common.helper

sealed class NetworkResult<T>(
    var data: T? = null,
    var message: String = "",
    var statusCode: Int? = null,
    var strResMessage: Int? = null
) {
    class Success<T>(data: T, statusCode: Int? = null) : NetworkResult<T>(data, statusCode = statusCode)
    class Error<T>(
        message: String = "",
        strResMessage: Int? = null,
        data: T? = null,
        statusCode: Int? = null
    ) : NetworkResult<T>(data, message, statusCode, strResMessage)

    class Loading<T> : NetworkResult<T>()
}