package com.mobile.reconnect.ui.login.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.UserApiClient
import com.mobile.reconnect.data.model.auth.SignUpRequest
import com.mobile.reconnect.data.network.ApiService
import com.mobile.reconnect.data.network.api.login.AuthApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val apiService: AuthApiService
) : ViewModel() {

    fun kakaoLogin(context: Context) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                Log.e("카카오 로그인 실패", "로그인 실패", error)
            } else if (token != null) {
                Log.i("카카오 로그인 성공", "로그인 성공 ${token.accessToken}")

                // 사용자 정보 가져오기
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("사용자 정보 가져오기 실패", "사용자 정보 가져오기 실패", error)
                    } else {
                        Log.i("사용자 정보", user.toString())

                        // 서버에 회원가입 요청
                        val signUpRequest = SignUpRequest(
                            email = user?.kakaoAccount?.email ?: "",
                            name = user?.kakaoAccount?.profile?.nickname ?: ""
                        )

                        signUp(signUpRequest, context)
                    }
                }
                Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signUp(signUpRequest: SignUpRequest, context: Context) {
        apiService.signUp(signUpRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("회원가입 성공", "회원가입 성공")
                    nextActivity(context)
                } else {
                    Log.e("회원가입 실패", "회원가입 실패 ${response.message()}")
                    Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("회원가입 실패", "네트워크 실패", t)
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun nextActivity(context: Context) {
        // 다음 액티비티로 이동하는 코드
    }
}
