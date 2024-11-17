package com.mobile.reconnect.data.model

data class ReportResponse(
    val reportId: Long,
    val missingPersonId: Long,
    val missingPersonName: String,
    val reportedAt: String,
    val foundImageUrls: List<String>
)