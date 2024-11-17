import java.io.FileInputStream
import java.util.Properties
import java.util.regex.Pattern.compile

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	id("kotlin-kapt")
	id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
	id("kotlin-parcelize")
	id("dagger.hilt.android.plugin")
}

val properties = Properties().apply {
	load(FileInputStream(rootProject.file("local.properties")))
}

android {
	namespace = "com.mobile.reconnect"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.mobile.reconnect"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
		buildConfigField("String", "KAKAO_LOGIN_KEY", properties.getProperty("KAKAO_LOGIN_KEY"))
		buildConfigField("String", "MAPS_API_KEY", properties.getProperty("MAPS_API_KEY"))
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		viewBinding = true
		dataBinding = true
		buildConfig = true
	}
}

// kapt 설정 추가: annotation processing의 오류를 줄이기 위해 추가
kapt {
	correctErrorTypes = true // annotation 처리 과정에서의 에러를 줄여줌
}

dependencies {
	// Core
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.constraintlayout)
	implementation(libs.androidx.annotation)

	// Material Components
	implementation(libs.material)
	// implementation(libs.androidx.material3) // Material 3.x가 필요하면 사용 (libs.material과 함께 사용 시 충돌 가능)

	// Navigation
	implementation(libs.androidx.navigation.fragment.ktx)
	implementation(libs.androidx.navigation.ui.ktx)

	// Lifecycle
	implementation(libs.androidx.lifecycle.livedata.ktx)
	implementation(libs.androidx.lifecycle.viewmodel.ktx)

	// Google Map and Location Services
	implementation(libs.play.services.maps)
	implementation(libs.play.services.location)
	implementation(libs.play.services.maps.v1802)
	implementation(libs.google.material)
	implementation(libs.maps.ktx)
	implementation(libs.maps.utils.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)


	// UI Tooling (Android Studio Preview)
	implementation(libs.androidx.ui.tooling.preview)

	// DataBinding
	implementation(libs.androidx.databinding.runtime)

	implementation(libs.androidx.activity)

	// Retrofit 라이브러리
	implementation (libs.retrofit)

	//OkHttp 라이브러리
	implementation ("com.squareup.okhttp3:okhttp:4.10.0")
	implementation(libs.androidx.espresso.core)
	implementation(files("libs/oauth-5.9.0.aar"))
	implementation ("com.android.support:appcompat-v7:28.0.0")
//	implementation("com.navercorp.nid:oauth:5.9.0")

	// Testing
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)

	// Glide 라이브러리 추가
	implementation("com.github.bumptech.glide:glide:4.12.0")
	kapt("com.github.bumptech.glide:compiler:4.12.0")

	// kakao
	implementation ("com.kakao.sdk:v2-user:2.15.0")
	implementation ("com.kakao.maps.open:android:2.12.8")

	// Network
	implementation ("com.squareup.retrofit2:retrofit:2.9.0")
	implementation ("com.squareup.okhttp3:okhttp-urlconnection:4.9.1")
	implementation ("com.squareup.okhttp3:okhttp:4.9.1")
	implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

	// Hilt
	implementation("com.google.dagger:hilt-android:2.48")
	kapt("com.google.dagger:hilt-android-compiler:2.48")

	// stomp
	implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")

	//rx
	implementation("io.reactivex.rxjava2:rxjava:2.2.5")
	implementation("io.reactivex.rxjava2:rxandroid:2.1.0")

	implementation("com.airbnb.android:lottie:5.0.0")
	implementation("androidx.security:security-crypto:1.1.0-alpha03")
}