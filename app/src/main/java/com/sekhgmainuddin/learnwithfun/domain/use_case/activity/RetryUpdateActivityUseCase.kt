package com.sekhgmainuddin.learnwithfun.domain.use_case.activity

import com.sekhgmainuddin.learnwithfun.data.db.LearnWithFunDao
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.UpdateActivityBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.HomeRepository
import javax.inject.Inject

class RetryUpdateActivityUseCase @Inject constructor(
    private val repository: HomeRepository,
    private val dao: LearnWithFunDao
) {
    suspend operator fun invoke() {
        val activities = dao.getAllActivity()
        activities.forEach {
            try {
                repository.updateActivity(UpdateActivityBodyParams(it.type, it.date, it.time))
                dao.deleteActivity(it)
            } catch (_: Exception) {

            }
        }
    }
}