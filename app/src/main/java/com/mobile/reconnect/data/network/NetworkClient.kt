package com.mobile.reconnect.data.network

import android.content.Context
import com.google.gson.GsonBuilder
import java.net.CookieManager
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.mobile.reconnect.BuildConfig
import com.mobile.reconnect.data.network.api.search.SearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // Hilt 모듈에 대한 의존성 주입 대상 지정
object NetworkClient {

	@Provides
	@Singleton
	fun provideOkHttpClient(): OkHttpClient {
		return OkHttpClient.Builder()
			.cookieJar(JavaNetCookieJar(CookieManager()))  // 쿠키 관리 설정
//			.readTimeOut(30, TimeUnit.SECONDS)
//			.writeTimeOut(30, TimeUnit.SECONDS)
			.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//		val gson = GsonBuilder()
//			.create()

		return Retrofit.Builder()
			.baseUrl(BuildConfig.BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	fun provideSearchApi(retrofit: Retrofit): SearchApi {
		return retrofit.create(SearchApi::class.java)
	}

	@Provides
	@Singleton
	fun provideApiService(retrofit: Retrofit): ApiService {
		return retrofit.create(ApiService::class.java)
	}

}
