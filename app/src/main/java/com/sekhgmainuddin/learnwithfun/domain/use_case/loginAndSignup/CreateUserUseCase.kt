package com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.Constants.SAVED_USER_DETAILS
import com.sekhgmainuddin.learnwithfun.common.enums.UserType
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.helper.PrefsHelper
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.saveAsJPG
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.CreateUserBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import com.sekhgmainuddin.learnwithfun.domain.repository.UploadFileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: LoginSignUpRepository,
    private val uploadFileRepository: UploadFileRepository,
    private val prefsHelper: PrefsHelper,
    @ApplicationContext val applicationContext: Context
) {
    operator fun invoke(
        name: String,
        email: String,
        countryCode: Int,
        phoneNumber: Long,
        userType: UserType,
        profilePicture: Bitmap?,
    ) = flow<NetworkResult<Unit>> {
        try {
            var photoUrl: String? = null
            if (profilePicture != null) {
                photoUrl =
                    uploadFileRepository.uploadFile(
                        profilePicture.saveAsJPG(
                            "${name}_Profile_Pic_${System.currentTimeMillis()}",
                            applicationContext
                        ) as Uri,
                        null,
                        "User/$email$countryCode$phoneNumber"
                    )
            }
            if (profilePicture != null && photoUrl == null) {
                emit(NetworkResult.Error(strResMessage = R.string.profile_pic_upload_failed))
            } else {
                val response = repository.createUser(
                    CreateUserBodyParams(
                        name,
                        email,
                        countryCode,
                        phoneNumber,
                        userType.name,
                        photoUrl
                    )
                )
                if (response.isSuccessful) {
                    prefsHelper.updateToken(response.body()!!.token)
                    prefsHelper.putValue(SAVED_USER_DETAILS, Json.encodeToString(response.body()))
                    emit(NetworkResult.Success(Unit))
                } else {
                    emit(NetworkResult.Error(message = response.message()))
                }
            }
        } catch (_: IOException) {
            emit(NetworkResult.Error(strResMessage = R.string.no_internet_please_check_your_internet_connection))
        } catch (e: Exception) {
            emit(NetworkResult.Error(strResMessage = R.string.default_error_message))
        }
    }
}