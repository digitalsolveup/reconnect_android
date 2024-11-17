package com.mobile.reconnect.ui.report

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.reconnect.R
import com.mobile.reconnect.databinding.FragmentReportBinding
import com.mobile.reconnect.ui.common.BaseFragment
import com.mobile.reconnect.ui.report.adapter.MissingPersonAdapter
import com.mobile.reconnect.ui.report.viewmodel.MissingPersonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report) {
	private val viewModel: MissingPersonViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Data Binding 설정
		binding.lifecycleOwner = viewLifecycleOwner
		binding.viewModel = viewModel

		// RecyclerView 설정
		setupRecyclerView()

		// ChipGroup 설정
		setupChipGroup()

		// 초기 데이터 로드: 거리순 정렬로 기본 설정
		viewModel.fetchMissingPersons("DISTANCE", 37.5665, 126.9780)
		binding.radiusGroup.check(R.id.radius_2km) // 기본 선택
	}

	private fun setupChipGroup() {
		binding.radiusGroup.setOnCheckedStateChangeListener { group, checkedIds ->
			if (checkedIds.isNotEmpty()) {
				when (checkedIds.first()) {
					R.id.radius_2km -> {
						viewModel.fetchMissingPersons("DISTANCE", 37.5665, 126.9780)
					}
					R.id.radius_3km -> {
						viewModel.fetchMissingPersons("REGISTRATION", 37.5665, 126.9780)
					}
					R.id.radius_4km -> {
						viewModel.fetchMissingPersons("REPORT_COUNT", 37.5665, 126.9780)
					}
				}
			}
		}
	}

	private fun setupRecyclerView() {
		val adapter = MissingPersonAdapter { missingPerson, view ->
			val bundle = Bundle().apply {
				putParcelable("missingPerson", missingPerson)
			}
			when (view.id) {
				R.id.btnReport -> {
					// btnReport 클릭 시 ReportRegistrationFragment로 이동
					findNavController().navigate(R.id.action_reportFragment_to_reportRegistrationFragment, bundle)
				}
				else -> {
					// 나머지 영역 클릭 시 ReportDetailFragment로 이동
					findNavController().navigate(R.id.action_reportFragment_to_reportDetailFragment, bundle)
				}
			}
		}

		binding.recyclerViewReportList.adapter = adapter
		binding.recyclerViewReportList.layoutManager = LinearLayoutManager(context)

		// LiveData 관찰 및 데이터 갱신
		viewModel.missingPersons.observe(viewLifecycleOwner) { missingPersons ->
			adapter.submitList(missingPersons)
			if (missingPersons.isNullOrEmpty()) {
				Log.e("ReportFragment", "Missing persons list is empty")
			} else {
				Log.d("ReportFragment", "Loaded ${missingPersons.size} items")
			}
		}
	}
}