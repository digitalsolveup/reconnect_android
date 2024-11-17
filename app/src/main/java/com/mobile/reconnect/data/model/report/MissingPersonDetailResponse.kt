package com.mobile.reconnect.data.model.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class MissingPersonDetailResponse(
    val name: String,
    val imageURL: String?,
    val specialFeature: String,
    val nationality: String,
    val gender: String,
    val age: Int,
    val height: Int,
    val weight: Int,
    val bodyType: String,
    val faceType: String,
    val tops: String,
    val bottoms: String,
    val shoes: String,
    val accessories: String,
    val hair: String,
    val lastSeenDateTime: String,
    val lastSeenLocation: String,
    val lastSeenLatitude: Double,
    val lastSeenLongitude: Double
) : Parcelable {
    val specialFeatureLabel: String
        get() = when (specialFeature) {
            "NONE" -> "없음"
            "NON_DISABLED_CHILD" -> "비장애아동"
            "DISABILITY" -> "장애"
            "DEMENTIA" -> "치매"
            "RUNAWAY" -> "가출인"
            "OTHER" -> "기타"
            else -> "알 수 없음"
        }

    val nationalityLabel: String
        get() = when (nationality) {
            "DOMESTIC" -> "내국인"
            "FOREIGN" -> "외국인"
            else -> "알 수 없음"
        }

    val genderLabel: String
        get() = when (gender) {
            "MALE" -> "남성"
            "FEMALE" -> "여성"
            else -> "알 수 없음"
        }

    val bodyTypeLabel: String
        get() = when (bodyType) {
            "SLIM" -> "마름"
            "AVERAGE" -> "보통"
            "OBESE" -> "비만"
            else -> "알 수 없음"
        }

    val faceTypeLabel: String
        get() = when (faceType) {
            "OVAL" -> "계란형"
            "SQUARE" -> "사각형"
            "LONG" -> "긴 얼굴"
            "TRIANGLE" -> "삼각형"
            else -> "알 수 없음"
        }

    val formattedLastSeenDateTime: String
        get() {
            return try {
                val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
                val dateTime = LocalDateTime.parse(lastSeenDateTime)
                dateTime.format(formatter)
            } catch (e: Exception) {
                "알 수 없음" // 파싱 실패 시 기본값
            }
        }
}