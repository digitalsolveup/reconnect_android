package com.mobile.reconnect.data.network

import java.net.CookieManager
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.mobile.reconnect.BuildConfig
import com.mobile.reconnect.data.network.api.ReportApi
import com.mobile.reconnect.data.network.api.report.MissingPersonApi
import com.mobile.reconnect.data.network.api.search.SearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
		return Retrofit.Builder()
			.baseUrl(BuildConfig.BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())  // Gson을 이용해 JSON 변환
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

	@Provides
	@Singleton
	fun provideMissingPersonApi(retrofit: Retrofit): MissingPersonApi {
		return retrofit.create(MissingPersonApi::class.java)
	}

	@Provides
	@Singleton
	fun provideReportApi(retrofit: Retrofit): ReportApi {
		return retrofit.create(ReportApi::class.java)
	}


}
