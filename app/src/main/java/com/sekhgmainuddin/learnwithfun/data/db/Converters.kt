package com.sekhgmainuddin.learnwithfun.data.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Base64.DEFAULT
import androidx.room.TypeConverter
import java.nio.ByteBuffer

class Converters {
    @TypeConverter
    fun bitmapToBase64(bitmap: Bitmap?): String? {
        return if (bitmap != null) {
            val byteBuffer = ByteBuffer.allocate(bitmap.height * bitmap.rowBytes)
            bitmap.copyPixelsToBuffer(byteBuffer)
            val byteArray = byteBuffer.array()
            Base64.encodeToString(byteArray, DEFAULT)
        } else {
            null
        }
    }

    @TypeConverter
    fun base64ToBitmap(base64String: String?): Bitmap? {
        return if(base64String!=null){
            val byteArray = Base64.decode(base64String, DEFAULT)
            BitmapFactory.decodeByteArray(
                byteArray,
                0, byteArray.size
            )
        }else {
            null
        }
    }
}