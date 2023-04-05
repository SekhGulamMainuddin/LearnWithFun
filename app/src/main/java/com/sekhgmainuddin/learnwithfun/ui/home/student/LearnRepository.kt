package com.sekhgmainuddin.learnwithfun.ui.home.student

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.sekhgmainuddin.learnwithfun.data.modals.Banner
import com.sekhgmainuddin.learnwithfun.utils.NetworkResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LearnRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference
) {

    private var _banners = MutableLiveData<NetworkResult<Banner>>()
    val banners: LiveData<NetworkResult<Banner>>
        get() = _banners

    suspend fun getBanners() {
        try {
            val result= firebaseFirestore.collection("Banners").document("banner").get().await()
            if (result.exists()){
                result.toObject(Banner::class.java)?.let {
                    _banners.postValue(NetworkResult.Success(it, 200))
                    Log.d("banners", "getBanners: $it")
                }
            }else{
                _banners.postValue(NetworkResult.Error("Not Found", statusCode = 404))
            }
        } catch (e: Exception) {
            Log.d("banners", "getBanners: $e")
            _banners.postValue(NetworkResult.Error(e.toString(), statusCode = 500))
        }
    }

}