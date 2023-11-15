package com.sekhgmainuddin.learnwithfun.domain.use_case.home

import com.sekhgmainuddin.learnwithfun.data.db.LearnWithFunDao
import javax.inject.Inject

class GetAllUploadFailedCheatAlerts @Inject constructor(
    private val dao: LearnWithFunDao,
) {
    operator fun invoke() = dao.getAllFailedCheatFlag()
    suspend operator fun invoke(unit: Unit) = dao.setFreshRetry()
}