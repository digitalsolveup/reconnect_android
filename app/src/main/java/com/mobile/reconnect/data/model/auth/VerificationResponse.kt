package com.mobile.reconnect.data.model.auth

data class VerificationResponse(
    val success: Boolean,
    val errorCode: Int,
    val errorMsg: String,
    val reqTime: Long,
    val resTime: Long,
    val version: String
)
