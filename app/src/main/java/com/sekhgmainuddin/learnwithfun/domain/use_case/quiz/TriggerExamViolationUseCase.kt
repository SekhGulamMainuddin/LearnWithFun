package com.sekhgmainuddin.learnwithfun.domain.use_case.quiz

import android.content.Context
import android.net.Uri
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
            var imageUrl = ""
            if (body.flagType != CheatingStatus.EXAM_WINDOW_CHANGED_DURING_TEST.name) {
                val userDetails = prefsHelper.getValue<UserDto>(SAVED_USER_DETAILS, null)!!
                imageUrl =
                    uploadFileRepository.uploadFile(
                        body.image?.saveAsJPG(
                            "${body.examId}_CheatFlag${body.flagType}${body.dateTime}",
                            applicationContext,
                            15
                        ) as Uri,
                        null,
                        "User/${userDetails.email}${userDetails.phone.countryCode}${userDetails.phone.phoneNumber}/Exams/${body.examId}${body.dateTime}"
                    )!!
                body.imageUrl = imageUrl
                dao.addFailedCheatFlag(body)
            }
            val response = repository.reportCheating(
                AddCheatFlagBodyParams(
                    body.examId,
                    CheatFlag(
                        body.flagType,
                        imageUrl,
                        body.dateTime
                    )
                )
            )
            if (response.isSuccessful) {
                dao.deleteFailedCheatFlag(body)
            } else {
                dao.addFailedCheatFlag(body)
            }
        } catch (e: Exception) {
            dao.addFailedCheatFlag(body)
        }
    }

}