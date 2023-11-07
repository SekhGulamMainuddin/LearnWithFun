package com.sekhgmainuddin.learnwithfun.data.repository

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.sekhgmainuddin.learnwithfun.domain.repository.UploadFileRepository
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class UploadFileRepositoryImpl @Inject constructor(
    private val storageReference: StorageReference
) : UploadFileRepository {
    override suspend fun uploadFile(uri: Uri?, file: File?, path: String): String? {
        return try {
            storageReference.child(path).putFile(uri ?: Uri.fromFile(file))
                .await().storage.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }
}