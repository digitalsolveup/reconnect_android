import java.io.FileInputStream
import java.util.Properties
import java.util.regex.Pattern.compile

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	id("kotlin-kapt")
	id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
	id("kotlin-parcelize")
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

	// Testing
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)

	// Glide 라이브러리
	implementation("com.github.bumptech.glide:glide:4.15.1")
	kapt("com.github.bumptech.glide:compiler:4.15.1")
}

