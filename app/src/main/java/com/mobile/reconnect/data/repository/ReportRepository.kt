package com.mobile.reconnect.data.repository

import com.mobile.reconnect.data.model.ReportGender
import com.mobile.reconnect.data.model.ReportRequest
import com.mobile.reconnect.data.network.api.ReportApi
import retrofit2.Response
import javax.inject.Inject

class ReportRepository @Inject constructor(private val api: ReportApi) {

	suspend fun createReport(reportRequest: ReportRequest): Response<Unit>? {
		return try {
			api.createReport(reportRequest)
		} catch (e: Exception) {
			null // 네트워크 에러 처리
		}
	}

	suspend fun updateGender(missingPersonId: Long, gender: com.mobile.reconnect.data.model.ReportGender): Response<Unit>? {
		return try {
			api.updateGender(missingPersonId, gender)
		} catch (e: Exception) {
			null
		}
	}
}