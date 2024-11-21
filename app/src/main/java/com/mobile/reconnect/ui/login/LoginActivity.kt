package com.mobile.reconnect.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
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
import com.mobile.reconnect.data.model.auth.SignUpRequest
import com.mobile.reconnect.databinding.ActivityLoginBinding
import com.mobile.reconnect.utils.Constants
import com.mobile.reconnect.ui.common.BaseActivity
import com.mobile.reconnect.ui.login.viewModel.LoginViewModel
import com.mobile.reconnect.ui.map.viewmodel.HomeBottomViewModel
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.oauth.view.NidOAuthLoginButton.Companion.launcher
import com.navercorp.nid.oauth.view.NidOAuthLoginButton.Companion.oauthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
	private val viewModel: LoginViewModel by viewModels()

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

//					if (error != null) {
//						Log.e("사용자 정보 가져오기 실패", "사용자 정보 가져오기 실패", error)
//					} else {
//						Log.i("사용자 정보", user.toString())
//
//						// 서버에 회원가입 요청
//						val signUpRequest = SignUpRequest(
//							email = user?.kakaoAccount?.email ?: "",
//							name = user?.kakaoAccount?.profile?.nickname ?: "",
//						)
//
//						viewModel.signUp(signUpRequest, context)
//					}
				}
				nextActivity()
			}
		}
	}

	private fun naverProfile() {
		NidOAuthLogin().callProfileApi( object : NidProfileCallback<NidProfileResponse> {
			override fun onSuccess(result: NidProfileResponse) {
				startActivity(Intent(this@LoginActivity, MainActivity::class.java))
				finish()
			}
			override fun onFailure(httpStatus: Int, message: String) {
				val errorCode = NaverIdLoginSDK.getLastErrorCode().code
				val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
				Toast.makeText(this@LoginActivity,"errorCode:$errorCode, errorDesc:$errorDescription",Toast.LENGTH_SHORT).show()
			}
			override fun onError(errorCode: Int, message: String) {
				onFailure(errorCode, message)
			}
		})
	}

	// 액티비티 생성 시 초기화
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		KakaoSdk.init(this, BuildConfig.KAKAO_LOGIN_KEY)
		NaverIdLoginSDK.initialize(this, BuildConfig.OAUTH_CLIENT_ID, BuildConfig.OAUTH_CLIENT_SECRET, BuildConfig.OAUTH_CLIENT_NAME)

		// 로그인 버튼 클릭 리스너 설정
		binding.btnKakaoLogin.setOnClickListener{
			kakaoLogin(this)
			finish()
		}
//		binding.buttonOAuthLoginImg.setOAuthLogin(oauthLoginCallback)

//		binding.buttonOAuthLoginImg.setOnClickListener {
//			NaverIdLoginSDK.authenticate(this, oauthLoginCallback!!)
//		}
	}

	private fun nextActivity() {
		startActivity(Intent(this, JoinActivity::class.java))
		finish()
	}
}
