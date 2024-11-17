package com.mobile.reconnect.ui.report.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.reconnect.data.model.report.MissingPersonDetailResponse
import com.mobile.reconnect.data.model.report.MissingPersonListResponse
import com.mobile.reconnect.data.repository.MissingPersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MissingPersonViewModel @Inject constructor(
	private val repository: MissingPersonRepository
) : ViewModel() {

	private val _missingPersons = MutableLiveData<List<MissingPersonListResponse>>()
	val missingPersons: LiveData<List<MissingPersonListResponse>> get() = _missingPersons

	private val _missingPersonDetail = MutableLiveData<MissingPersonDetailResponse>()
	val missingPersonDetail: LiveData<MissingPersonDetailResponse> get() = _missingPersonDetail

	private val _currentTime = MutableLiveData<String>()
	val currentTime: LiveData<String> get() = _currentTime

	init {
		updateCurrentTime()
	}

	fun fetchMissingPersons(sortBy: String, latitude: Double, longitude: Double) {
		viewModelScope.launch {
			try {
				val response = repository.getMissingPersons(sortBy, latitude, longitude)
				_missingPersons.postValue(response)
			} catch (e: Exception) {
				Log.e("MissingPersonViewModel", "Error fetching persons: ${e.message}")
			}
		}
	}

	fun fetchMissingPersonDetail(id: Long) {
		viewModelScope.launch {
			try {
				val response = repository.getMissingPersonDetails(id)
				_missingPersonDetail.postValue(response)
			} catch (e: Exception) {
				Log.e("MissingPersonViewModel", "Error fetching details: ${e.message}")
			}
		}
	}

	private fun updateCurrentTime() {
		viewModelScope.launch {
			while (true) {
				val dateFormat = SimpleDateFormat("HH", Locale.getDefault())
				val currentTimeString = dateFormat.format(Date())
				_currentTime.postValue(currentTimeString)
				delay(60 * 1000L) // 1분마다 업데이트
			}
		}
	}
}