package com.sekhgmainuddin.learnwithfun.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sekhgmainuddin.learnwithfun.data.db.entities.ActivityEntity
import com.sekhgmainuddin.learnwithfun.data.db.entities.CheatFlagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LearnWithFunDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFailedCheatFlag(cheatFlag: CheatFlagEntity)

    @Delete
    suspend fun deleteFailedCheatFlag(cheatFlag: CheatFlagEntity)

    @Update
    suspend fun updateFailedCheatFlag(cheatFlag: CheatFlagEntity)

    @Query("SELECT * FROM cheat_flag_entity")
    fun getAllFailedCheatFlag() : Flow<List<CheatFlagEntity>>

    @Query("UPDATE cheat_flag_entity SET retryCount=4 WHERE retryCount=5")
    suspend fun setFreshRetry()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addActivity(activityEntity: ActivityEntity)

    @Delete
    suspend fun deleteActivity(activityEntity: ActivityEntity)

    @Query("SELECT * FROM activity_entity WHERE date=:date AND type=:type")
    suspend fun getActivityByDate(date: String, type: String) : ActivityEntity?

    @Update
    suspend fun updateActivity(activityEntity: ActivityEntity)

    @Query("SELECT * FROM activity_entity")
    suspend fun getAllActivity() : List<ActivityEntity>

}