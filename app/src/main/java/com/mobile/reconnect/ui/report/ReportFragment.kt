package com.mobile.reconnect.ui.report

import android.os.Bundle
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

		binding.lifecycleOwner = viewLifecycleOwner // LiveData 바인딩 활성화

		binding.radiusGroup.setOnCheckedChangeListener { _, checkedId ->
			when (checkedId) {
				R.id.radius_2km -> viewModel.fetchMissingPersons("DISTANCE", 37.5665, 126.9780)
				R.id.radius_3km -> viewModel.fetchMissingPersons("REGISTRATION", 37.5665, 126.9780)
				R.id.radius_4km -> viewModel.fetchMissingPersons("REPORT_COUNT", 37.5665, 126.9780)
			}
		}

		setupRecyclerView()

		viewModel.fetchMissingPersons("DISTANCE", 37.5665, 126.9780)

		// 관찰하여 데이터를 리사이클러뷰에 갱신
		viewModel.missingPersons.observe(viewLifecycleOwner) { missingPersons ->
			(binding.recyclerViewReportList.adapter as? MissingPersonAdapter)?.submitList(missingPersons)
		}
	}

	private fun setupRecyclerView() {
		binding.recyclerViewReportList.adapter = MissingPersonAdapter { missingPerson, view ->
			when (view.id) {
				R.id.btnReport -> {
					val bundle = Bundle().apply {
						putParcelable("missingPerson", missingPerson)
					}
					findNavController().navigate(R.id.action_reportFragment_to_reportRegistrationFragment, bundle)
				}
				else -> {
					val bundle = Bundle().apply {
						putParcelable("missingPerson", missingPerson)
					}
					findNavController().navigate(R.id.action_reportFragment_to_reportDetailFragment, bundle)
				}
			}
		}

		// 레이아웃 매니저 설정
		binding.recyclerViewReportList.layoutManager = LinearLayoutManager(context)
	}
}