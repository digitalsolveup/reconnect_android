package com.mobile.reconnect.ui.report.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.reconnect.data.model.ReportRequest
import com.mobile.reconnect.data.model.report.ReportGender
import com.mobile.reconnect.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportRegistrationViewModel @Inject constructor(
	private val reportRepository: ReportRepository
) : ViewModel() {

	fun submitReport(reportRequest: ReportRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
		viewModelScope.launch {
			try {
				val response = reportRepository.createReport(reportRequest)
				if (response?.isSuccessful == true) {
					onSuccess()
				} else {
					onError("제보 등록 실패: ${response?.message() ?: "알 수 없음"}")
				}
			} catch (e: Exception) {
				onError("제보 등록 중 오류 발생: ${e.message}")
			}
		}
	}

	fun updateGender(
		missingPersonId: Long,
		gender: com.mobile.reconnect.data.model.ReportGender, // 올바른 타입
		onSuccess: () -> Unit,
		onError: (String) -> Unit
	) {
		viewModelScope.launch {
			try {
				val response = reportRepository.updateGender(missingPersonId, gender)
				if (response?.isSuccessful == true) {
					onSuccess()
				} else {
					onError("성별 업데이트 실패: ${response?.message() ?: "알 수 없음"}")
				}
			} catch (e: Exception) {
				onError("성별 업데이트 중 오류 발생: ${e.message}")
			}
		}
	}
}