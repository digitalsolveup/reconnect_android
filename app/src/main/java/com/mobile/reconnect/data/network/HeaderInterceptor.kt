package com.software.somding.network

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
	private val prefs: SharedPreferences
) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val token = prefs.getString("token", null)

		val request = chain.request().newBuilder()
			.apply {
				token?.let {
					addHeader("Authorization", "Bearer $it")
				}
			}
			.build()

		return chain.proceed(request)
	}
}
