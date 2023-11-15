package com.sekhgmainuddin.learnwithfun.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sekhgmainuddin.learnwithfun.data.db.entities.CheatFlagEntity

@TypeConverters(Converters::class)
@Database(
    entities = [CheatFlagEntity::class],
    version = 5,
    exportSchema = false
)
abstract class LearnWithFunDb : RoomDatabase() {
    abstract fun getDao(): LearnWithFunDao
}