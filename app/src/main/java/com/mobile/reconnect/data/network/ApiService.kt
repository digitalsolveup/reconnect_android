package com.mobile.reconnect.data.network

import com.mobile.reconnect.data.model.ReportGender
import com.mobile.reconnect.data.model.ReportRequest
import retrofit2.Response // 올바른 Response 임포트
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/reports")
    suspend fun createReport(@Body reportRequest: ReportRequest): Response<Unit>

    @PATCH("/missing-persons/{id}/gender")
    suspend fun updateGender(
        @Path("id") missingPersonId: Long,
        @Body gender: Map<String, ReportGender> // 서버에 맞게 Key-Value로 보낼 경우
    ): Response<Unit>
}