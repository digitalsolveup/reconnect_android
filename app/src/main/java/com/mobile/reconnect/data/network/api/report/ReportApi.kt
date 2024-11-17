package com.mobile.reconnect.data.network.api

import com.mobile.reconnect.data.model.ReportGender
import com.mobile.reconnect.data.model.ReportRequest
import com.mobile.reconnect.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class ReportApi @Inject constructor(private val apiService: ApiService) {

	suspend fun createReport(reportRequest: ReportRequest): Response<Unit> {
		return apiService.createReport(reportRequest)
	}

	suspend fun updateGender(missingPersonId: Long, gender: ReportGender): Response<Unit> {
		return apiService.updateGender(missingPersonId, mapOf("gender" to gender))
	}
}