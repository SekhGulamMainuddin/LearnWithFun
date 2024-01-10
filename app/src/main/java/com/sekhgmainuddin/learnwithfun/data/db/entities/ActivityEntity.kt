package com.sekhgmainuddin.learnwithfun.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_entity", primaryKeys = ["date", "time"])
data class ActivityEntity(
    @ColumnInfo("date") val date: String,
    @ColumnInfo("type") val type: String,
    @ColumnInfo("time") var time: Long
)