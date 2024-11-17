package com.mobile.reconnect.data.model

data class ReportRequest(
    val missingPersonId: Long,
    val gender: ReportGender,
    val ageGroup: AgeGroup?,
    val specialFeature: ReportSpecialFeature?,
    val tops: Boolean = false,
    val bottoms: Boolean = false,
    val shoes: Boolean = false,
    val accessories: Boolean = false,
    val hair: Boolean = false,
    val foundImageUrls: List<String>? = emptyList(),
    val locationFound: String,
    val foundLatitude: Double,
    val foundLongitude: Double,
    val additionalDescription: String? = null,
    val surroundingImageUrls: List<String>? = emptyList(),
    val additionalReport: String? = null
)

enum class ReportGender(val label: String) { // 성별
    MALE("남자"),
    FEMALE("여자")
}

enum class AgeGroup(val label: String) { // 나이대 (10대 미만 ~ 80대 이상)
    UNDER_10("10대 미만"),
    TEENAGER("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대"),
    SIXTIES("60대"),
    SEVENTIES("70대"),
    OVER_EIGHTY("80대 이상")
}

enum class ReportSpecialFeature(val label: String) { // 특이사항
    NONE("없음"),
    DISABILITY("장애"),
    DEMENTIA("치매")
}