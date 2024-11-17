package com.mobile.reconnect.data.repository

import com.mobile.reconnect.data.model.report.MissingPersonDetailResponse
import com.mobile.reconnect.data.model.report.MissingPersonListResponse
import com.mobile.reconnect.data.network.api.report.MissingPersonApi
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MissingPersonRepository @Inject constructor(
	private val missingPersonApi: MissingPersonApi
) {
	suspend fun getMissingPersons(sortBy: String, latitude: Double, longitude: Double): List<MissingPersonListResponse> {
		return missingPersonApi.getMissingPersons(sortBy, latitude, longitude)
	}

	suspend fun getMissingPersonDetails(id: Long): MissingPersonDetailResponse {
		return missingPersonApi.getMissingPersonDetails(id)
	}
}