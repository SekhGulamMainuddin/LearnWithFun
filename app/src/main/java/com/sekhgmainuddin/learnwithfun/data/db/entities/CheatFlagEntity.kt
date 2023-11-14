package com.sekhgmainuddin.learnwithfun.data.db.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sekhgmainuddin.learnwithfun.data.db.Converters

@Entity(tableName = "cheat_flag_entity")
data class CheatFlagEntity(
    @ColumnInfo(name = "dateTime") @PrimaryKey(autoGenerate = false) val dateTime: Long,
    @ColumnInfo(name = "examId") val examId: String,
    @ColumnInfo(name = "flagType") val flagType: String,
    @ColumnInfo(name = "image") @TypeConverters(Converters::class) var image: Bitmap?,
    @ColumnInfo(name = "imageUrl") var imageUrl: String? = null,
)