package com.mobile.reconnect.data.model.report

import java.time.LocalDateTime

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
data class Report(
    val reportId: Int, // 제보 ID
    val missingPersonId: Int, // 제보된 실종자 ID (연결을 위한 값)
    val gender: ReportGender, // 제보된 성별
    val ageGroup: AgeGroup, // 제보된 나이대
    val specialFeature: ReportSpecialFeature, // 제보된 특이사항
    val selectedClothingItems: List<String>, // 인상착의 선택 (체크박스 선택으로 구성됨)
    val foundImageUrl: String?, // 실종자 사진 첨부 (카메라 촬영 이미지 URL)
    val imageResId: Int,
    val locationFound: String, // 발견 위치
    val additionalDescription: String, // 추가 설명
    val surroundingImageUrl: String?, // 주변 사진 첨부 (카메라 촬영 이미지 URL)
    val additionalReport: String?, // 추가 제보
    val reportedAt: LocalDateTime // 제보 시각
)
