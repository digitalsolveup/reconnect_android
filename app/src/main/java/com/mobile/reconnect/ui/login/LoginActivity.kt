package com.mobile.reconnect.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.mobile.reconnect.BuildConfig
import com.mobile.reconnect.MainActivity
import com.mobile.reconnect.R
import com.mobile.reconnect.databinding.ActivityLoginBinding
import com.mobile.reconnect.utils.Constants
import com.mobile.reconnect.ui.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

	private fun kakaoLogin(context: Context){
		// 카카오톡으로 로그인
		UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
			if (error != null) {
				Log.e("너무 잠이 온다", "zzzzz로그인 실패", error)
			}
			else if (token != null) {
				Log.i("너무 잠이 온다", "로그인 성공 ${token.accessToken}")
				UserApiClient.instance.me { user, error ->
					Log.i("kj308", user.toString())
				}
				Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show()
				nextActivity()
			}
		}
	}

	// 액티비티 생성 시 초기화
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// 카카오 SDK 초기화
//		Log.d(Constants.TAG, "keyhash : ${Utility.getKeyHash(this)}")
		KakaoSdk.init(this, BuildConfig.KAKAO_LOGIN_KEY)

		// 로그인 버튼 클릭 리스너 설정
		binding.btnKakaoLogin.setOnClickListener{
			kakaoLogin(this)
		}

		binding.btnNaverLogin.setOnClickListener {
			startActivity(Intent(this, MainActivity::class.java))
			finish()
		}
	}

	// 로그인 후 다음 액티비티로 이동
	private fun nextActivity() {
		startActivity(Intent(this, JoinActivity::class.java))
		finish() // 로그인 후 현재 액티비티 종료
	}
}
