package com.mobile.reconnect.ui.alarm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.reconnect.data.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    private var onAlarmReceived: ((title: String, content: String) -> Unit)? = null

    fun startAlarmStream() {
        alarmRepository.connectToAlarmStream(
            onAlarmReceived = { title, content ->
                Log.d("Alarm", "$title: $content")
                this.onAlarmReceived?.invoke(title, content)
            },
            onError = { error ->
                Log.e("Alarm", "SSE 연결 실패: ${error.message}")
            }
        )

        generateMockAlarms()
    }

    private fun generateMockAlarms() {
        viewModelScope.launch {
            while (isActive) {
                delay(60_000)
                val title = "테스트 알림"
                val content = "이것은 테스트 알림입니다."
                Log.d("MockAlarm", "$title: $content")
                onAlarmReceived?.invoke(title, content)
            }
        }
    }

}
