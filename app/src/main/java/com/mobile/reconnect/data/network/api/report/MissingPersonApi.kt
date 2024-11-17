package com.mobile.reconnect.data.network.api.report

import com.mobile.reconnect.data.model.report.MissingPersonDetailResponse
import com.mobile.reconnect.data.model.report.MissingPersonListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MissingPersonApi {
	@GET("/api/missing_person")
	suspend fun getMissingPersons(
		@Query("sortBy") sortBy: String,
		@Query("currentLatitude") latitude: Double,
		@Query("currentLongitude") longitude: Double
	): List<MissingPersonListResponse>

	@GET("/api/missing_person/{id}")
	suspend fun getMissingPersonDetails(
		@Path("id") id: Long
	): MissingPersonDetailResponse
}