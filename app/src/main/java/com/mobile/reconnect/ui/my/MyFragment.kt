package com.mobile.reconnect.ui.my

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mobile.reconnect.R
import com.mobile.reconnect.databinding.FragmentMyBinding
import com.mobile.reconnect.ui.my.viewmodel.MyViewModel
import com.mobile.reconnect.ui.common.BaseFragment
import com.software.somding.presentation.common.NavigationUtil.navigate

class MyFragment : BaseFragment<FragmentMyBinding>(R.layout.fragment_my) {
	private val viewModel: MyViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.cardRegister.setOnClickListener { navigate(R.id.action_navigation_mypage_to_navigation_my_preperson) }
		binding.tvMyInfoUpdate.setOnClickListener { navigate(R.id.action_navigation_mypage_to_navigation_my_info) }
		binding.tvMyReport.setOnClickListener { navigate(R.id.action_navigation_mypage_to_navigation_my_report) }
		binding.tvMyNotification.setOnClickListener { navigate(R.id.action_navigation_mypage_to_navigation_my_noti) }
		binding.cardReport.setOnClickListener {
//			navigate(R.id.action_navigation_mypage_to_navigation_file_report)
			val myUri = Uri.parse("tel:182")
			val myIntent = Intent(Intent.ACTION_DIAL, myUri)
			startActivity(myIntent)
		}
	}

}