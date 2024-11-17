package com.software.somding.network

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
	private val sharedPreferences: SharedPreferences
) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()
		val token = sharedPreferences.getString("accessToken", null)

		Log.d("AuthInterceptor", "Token: $token")

		return if (!token.isNullOrEmpty()) {
			val requestWithAuth = originalRequest.newBuilder()
				.header("Authorization", "Bearer $token")
				.build()
			chain.proceed(requestWithAuth)
		} else {
			chain.proceed(originalRequest)
		}
	}
}
