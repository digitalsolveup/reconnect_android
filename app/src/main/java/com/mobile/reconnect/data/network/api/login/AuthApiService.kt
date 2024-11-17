package com.mobile.reconnect.data.network.api.login

import com.mobile.reconnect.data.model.auth.VerificationResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {
    @POST("/api/auth/verification/kakao")
    suspend fun verifyKakaoLogin(
        @Query("code") accessToken: String
    ): Response<VerificationResponse>
}