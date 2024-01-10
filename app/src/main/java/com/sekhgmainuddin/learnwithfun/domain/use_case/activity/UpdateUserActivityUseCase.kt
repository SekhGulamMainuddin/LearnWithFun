package com.sekhgmainuddin.learnwithfun.domain.use_case.activity

import android.util.Log
import com.sekhgmainuddin.learnwithfun.data.db.LearnWithFunDao
import com.sekhgmainuddin.learnwithfun.data.db.entities.ActivityEntity
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.UpdateActivityBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.HomeRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateUserActivityUseCase @Inject constructor(
    private val repository: HomeRepository,
    private val dao: LearnWithFunDao
) {
    suspend operator fun invoke(body: UpdateActivityBodyParams) {
        try {
            withContext(NonCancellable){
                try {
                    repository.updateActivity(body)
                } catch (e: Exception) {
                    val activityEntity = dao.getActivityByDate(body.date, body.type)
                    if (activityEntity == null) {
                        dao.addActivity(ActivityEntity(body.date, body.type, body.time))
                    }else{
                        activityEntity.time += body.time
                        dao.updateActivity(activityEntity)
                    }
                }
            }
        }catch (_: Exception){

        }
    }
}