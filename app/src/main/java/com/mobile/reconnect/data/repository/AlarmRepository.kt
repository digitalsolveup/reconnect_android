package com.mobile.reconnect.data.repository

import com.mobile.reconnect.BuildConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepository @Inject constructor(
    private val client: OkHttpClient
) {

    /**
     * SSE 알림 스트림 연결
     * @param onAlarmReceived 알림 수신 시 처리할 콜백 함수
     * @param onError 에러 발생 시 처리할 콜백 함수
     */
    fun connectToAlarmStream(
        onAlarmReceived: (title: String, content: String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val request = Request.Builder()
            .url("${BuildConfig.BASE_URL}/api/alarm/stream")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    body.source().use { source ->
                        while (!source.exhausted()) {
                            val line = source.readUtf8Line()
                            if (line != null && line.startsWith("data:")) {
                                val json = line.substring(5).trim()
                                try {
                                    val alarmData = JSONObject(json)
                                    val title = alarmData.optString("title", "알림")
                                    val content = alarmData.optString("content", "내용이 없습니다")
                                    onAlarmReceived(title, content)
                                } catch (e: Exception) {
                                    onError(e)
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}
