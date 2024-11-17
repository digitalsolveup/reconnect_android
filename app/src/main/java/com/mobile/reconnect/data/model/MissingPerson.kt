package com.mobile.reconnect.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class SpecialFeature(val label: String) { // 특이사항
	NONE("없음"),
	DISABILITY("장애"),
	DEMENTIA("치매")
}

enum class Nationality(val label: String) { // 내국인, 외국인 여부
	DOMESTIC("내국인"),
	FOREIGN("외국인")
}

enum class Gender(val label: String) { // 성별
	MALE("남자"),
	FEMALE("여자")
}

enum class BodyType(val label: String) { // 체격
	SLIM("마름"),
	AVERAGE("보통"),
	OBESE("살찜")
}

enum class FaceType(val label: String) { // 얼굴형
	OVAL("계란형"),
	SQUARE("사각형"),
	LONG("긴형"),
	TRIANGLE("역삼각형")
}

@Parcelize
data class MissingPerson(
	val id: Int,
	val name: String,
	val imageURL: String,
	val imageResId: Int,
	val specialFeature: SpecialFeature,
	val nationality: Nationality,
	val gender: Gender,
	val age: Int,
	val height: Int,
	val weight: Int,
	val bodyType: BodyType,
	val faceType: FaceType,
	val tops: String, // 상의
	val bottoms: String, // 하의
	val shoes: String,
	val accessories: String,
	val hair: String,
	val lastSeenDateTime: LocalDateTime, // 실종 발생 일시
	val lastSeenLocation: String// 실종 발생 위치
) : Parcelable {
	val formattedLastSeenDateTime: String
		get() = lastSeenDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
}