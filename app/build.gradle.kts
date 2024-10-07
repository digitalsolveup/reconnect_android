plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	id("kotlin-kapt")
	id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.androidx.constraintlayout)

	implementation(libs.androidx.material3)
	implementation(libs.androidx.ui.tooling.preview)

	// Navigation
	implementation(libs.androidx.navigation.fragment.ktx)
	implementation(libs.androidx.navigation.ui.ktx)

	implementation(libs.androidx.lifecycle.livedata.ktx)
	implementation(libs.androidx.lifecycle.viewmodel.ktx)

	// Test
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)

	implementation(libs.androidx.annotation)
	implementation(libs.androidx.databinding.runtime)

	implementation(libs.play.services.maps)
	implementation(libs.play.services.location)
	implementation(libs.material.v140alpha02)

	implementation(libs.google.material)
	implementation(libs.material.v190)

}
