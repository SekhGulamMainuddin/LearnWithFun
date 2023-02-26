package com.sekhgmainuddin.learnwithfun.ui.login

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.sekhgmainuddin.learnwithfun.data.modals.User
import com.sekhgmainuddin.learnwithfun.utils.NetworkResult
import com.sekhgmainuddin.learnwithfun.utils.Utils.getBitmap
import com.sekhgmainuddin.learnwithfun.utils.Utils.getFileExtension
import com.sekhgmainuddin.learnwithfun.utils.Utils.saveAsJPG
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginSignUpRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    @ApplicationContext val context: Context
) {

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    private val _result = MutableLiveData<NetworkResult<FirebaseUser>>()
    val result: LiveData<NetworkResult<FirebaseUser>>
        get() = _result

    suspend fun loginEmail(email: String, password: String) {
        try {
            val response = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Log.d(
                "loginTimeShare",
                "loginEmail: ${response.additionalUserInfo.toString()} ${response.user.toString()}"
            )
            _result.postValue(NetworkResult.Success(response.user!!, 200))
        } catch (firebaseAuthException: FirebaseAuthInvalidUserException) {
            _result.postValue(NetworkResult.Error("User Not Found", statusCode = 404))
        } catch (invalidCredentials: FirebaseAuthInvalidCredentialsException) {
            _result.postValue(NetworkResult.Error("Invalid Credentials", statusCode = 403))
        } catch (e: Exception) {
            Log.d("loginTimeShare", "loginEmail: ${e.localizedMessage} ${e.javaClass}")
            _result.postValue(NetworkResult.Error(e.localizedMessage, statusCode = 500))
        }

    }

    suspend fun googleLoginOrSignUp(firebaseCredential: AuthCredential) {
        try {
            val response = firebaseAuth.signInWithCredential(firebaseCredential).await()
            if (response.additionalUserInfo?.isNewUser == true) {
                val user = response.user
                val newUser = User(
                    user?.displayName ?: "",
                    "",
                    user?.email ?: "",
                    user?.phoneNumber ?: "",
                    user?.photoUrl.toString(),
                    "",
                    null,
                    null,
                    1,
                    null
                )
                user?.uid?.let {
                    newUser.userId= it
                    firebaseFirestore.collection("Users").document(it).set(newUser).await()
                }
                _result.postValue(NetworkResult.Success(response.user!!, 201))
            }
            else
                _result.postValue(NetworkResult.Success(response.user!!, 200))
        } catch (e: Exception) {
            _result.postValue(NetworkResult.Error(e.localizedMessage, statusCode = 500))
        }
    }

    private val _signUpResult = MutableLiveData<NetworkResult<FirebaseUser>>()
    val signUpResult: LiveData<NetworkResult<FirebaseUser>>
        get() = _signUpResult

    suspend fun signUpEmail(email: String, password: String) {
        try {
            val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            _signUpResult.postValue(NetworkResult.Success(response.user!!, 201))
        } catch (e: com.google.firebase.FirebaseException){
            _signUpResult.postValue(
                NetworkResult.Error(
                    "Internal Server Error Occurred",
                    statusCode = 500
                )
            )
        }
        catch (e: Exception) {
            Log.d("signUpEmail", "signUpEmail: $e")
        }
    }

    private val _newUserDetailUpload = MutableLiveData<NetworkResult<String>>()
    val newUserDetailUpload: LiveData<NetworkResult<String>>
        get() = _newUserDetailUpload

    suspend fun uploadNewUserDetail(email: String, phone: String,
        imageUri: Uri?, bitmap: Bitmap?,
        name: String, bio: String, location: String,
        interests: ArrayList<String>
    ) {
        val detailMap = HashMap<String, Any>()
        try {
            try {
                var uri: Uri?= imageUri?.getBitmap(context.contentResolver)?.saveAsJPG(firebaseAuth.uid.toString(),context) as Uri
                if (bitmap!=null){
                    uri = bitmap.saveAsJPG(firebaseAuth.uid.toString(),context) as Uri
                }
                val imageResponse = uri?.let {

                    storageReference.child(
                        "ProfileImage/" + firebaseAuth.uid + "." + getFileExtension(uri,context))
                        .putFile(it).await()
                }
                val uriTask = imageResponse?.storage?.downloadUrl
                while (!uriTask?.isSuccessful!!){}
                val downloadUrl = uriTask.result
                val download_url = downloadUrl.toString()
                detailMap["imageUrl"] = download_url
            } catch (e: Exception) {
                _newUserDetailUpload.postValue(NetworkResult.Error("Image Upload Failed using Default Profile Image", statusCode = 409))
                Log.d("imageUploadException", "uploadNewUserDetail: $e")
                detailMap["imageUrl"] = "https://firebasestorage.googleapis.com/v0/b/time-share-30ac6.appspot.com/o/ProfileImage%2Fdefault_profile_pic.png?alt=media&token=116dce19-d848-481c-b081-389f4bf598ea"
            }
            detailMap["email"] = email
            detailMap["phone"] = phone
            detailMap["name"] = name
            detailMap["bio"] = bio
            detailMap["location"] = location
            detailMap["interests"] = interests
            firebaseAuth.uid?.let { firebaseFirestore.collection("Users").document(it).set(detailMap).await() }
            _newUserDetailUpload.postValue(NetworkResult.Success("Profile Details Added Successfully",200))
        } catch (e: Exception) {
            Log.d("uploadNewUser", "uploadNewUserDetail: $e")
            _newUserDetailUpload.postValue(NetworkResult.Error("Some Error Occurred",statusCode = 400))
        }
    }

    private val _phoneLoginSignUp = MutableLiveData<NetworkResult<FirebaseUser>>()
    val phoneLoginSignUp: LiveData<NetworkResult<FirebaseUser>>
        get() = _phoneLoginSignUp

    suspend fun phoneLoginSignUp(credential: PhoneAuthCredential){
        try {
            val response= firebaseAuth.signInWithCredential(credential).await()
            if (response.additionalUserInfo?.isNewUser == true)
                _phoneLoginSignUp.postValue(NetworkResult.Success(response.user!!, statusCode = 201))
            else
                _phoneLoginSignUp.postValue(NetworkResult.Success(response.user!!, statusCode = 200))
        }catch (e: Exception){
            Log.d("exceptionPhoneLogin", "phoneNewUser: $e")
        }
    }

}