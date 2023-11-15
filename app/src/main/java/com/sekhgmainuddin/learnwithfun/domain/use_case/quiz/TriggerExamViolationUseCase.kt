package com.sekhgmainuddin.learnwithfun.domain.use_case.quiz

import android.content.Context
import android.net.Uri
import android.util.Log
import com.sekhgmainuddin.learnwithfun.common.Constants.SAVED_USER_DETAILS
import com.sekhgmainuddin.learnwithfun.common.enums.CheatingStatus
import com.sekhgmainuddin.learnwithfun.common.helper.PrefsHelper
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.saveAsJPG
import com.sekhgmainuddin.learnwithfun.data.db.LearnWithFunDao
import com.sekhgmainuddin.learnwithfun.data.db.entities.CheatFlagEntity
import com.sekhgmainuddin.learnwithfun.data.dto.UserDto
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.AddCheatFlagBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.CheatFlag
import com.sekhgmainuddin.learnwithfun.domain.repository.ExamRepository
import com.sekhgmainuddin.learnwithfun.domain.repository.UploadFileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TriggerExamViolationUseCase @Inject constructor(
    private val repository: ExamRepository,
    private val uploadFileRepository: UploadFileRepository,
    private val dao: LearnWithFunDao,
    private val prefsHelper: PrefsHelper,
    @ApplicationContext private val applicationContext: Context
) {
    suspend operator fun invoke(body: CheatFlagEntity) {
        try {
            if (body.flagType != CheatingStatus.EXAM_WINDOW_CHANGED_DURING_TEST.name && body.imageUrl==null) {
                val userDetails = prefsHelper.getValue<UserDto>(SAVED_USER_DETAILS, null)!!
                body.imageUrl =
                    uploadFileRepository.uploadFile(
                        body.image?.saveAsJPG(
                            "${body.examId}_CheatFlag${body.flagType}${body.dateTime}",
                            applicationContext,
                            15
                        ) as Uri,
                        null,
                        "User/${userDetails.email}${userDetails.phone.countryCode}${userDetails.phone.phoneNumber}/Exams/${body.examId}${body.dateTime}${body.flagType}"
                    )!!
                dao.addFailedCheatFlag(body)
            }
            val response = repository.reportCheating(
                AddCheatFlagBodyParams(
                    body.examId,
                    CheatFlag(
                        body.flagType,
                        body.imageUrl!!,
                        body.dateTime
                    )
                )
            )
            if (response.isSuccessful) {
                Log.d("ExamViolation", "invoke: ISSUCCESSFULL")
                dao.deleteFailedCheatFlag(body)
            } else {
                body.retryCount++
                Log.d("ExamViolation", "invoke: FAILED")
                dao.addFailedCheatFlag(body)
            }
        } catch (e: Exception) {
            Log.d("ExamViolation", "invoke: $e")
            body.retryCount++
            dao.addFailedCheatFlag(body)
        }
    }

}