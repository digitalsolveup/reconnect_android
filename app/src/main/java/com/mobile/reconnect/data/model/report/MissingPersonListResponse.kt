package com.mobile.reconnect.data.model.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MissingPersonListResponse(
    val id: Long,
    val name: String,
    val imageURL: String,
    val gender: String,
    val age: Int,
    val height: Int,
    val weight: Int,
    val specialFeature: String,
    val tops: String,
    val bottoms: String,
    val shoes: String,
    val accessories: String,
    val hair: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    val genderLabel: String
        get() = when (gender) {
            "MALE" -> "남성"
            "FEMALE" -> "여성"
            else -> "알 수 없음"
        }

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
}