package com.sekhgmainuddin.learnwithfun.domain.repository

import android.net.Uri
import java.io.File

interface UploadFileRepository {
    suspend fun uploadFile(uri: Uri?, file: File?, path: String) : String?
}