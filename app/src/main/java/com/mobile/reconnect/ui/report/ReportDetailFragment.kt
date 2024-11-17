package com.mobile.reconnect.ui.report

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mobile.reconnect.R
import com.mobile.reconnect.data.model.report.MissingPersonListResponse
import com.mobile.reconnect.databinding.FragmentReportDetailBinding
import com.mobile.reconnect.ui.common.BaseFragment
import com.mobile.reconnect.ui.report.viewmodel.MissingPersonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportDetailFragment : BaseFragment<FragmentReportDetailBinding>(R.layout.fragment_report_detail) {
    private val viewModel: MissingPersonViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 바인딩 초기화
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // 전달된 데이터 확인
        val missingPerson = arguments?.getParcelable<MissingPersonListResponse>("missingPerson")
        if (missingPerson != null) {
            viewModel.fetchMissingPersonDetail(missingPerson.id)
        } else {
            Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }

        // LiveData 관찰 설정
        setupObservers()

        // 뒤로 가기 버튼 설정
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // reportbtn 클릭 시 ReportRegistrationFragment로 이동
        binding.reportbtn.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("missingPerson", missingPerson)
            }
            findNavController().navigate(R.id.action_reportDetailFragment_to_reportRegistrationFragment, bundle)
        }
    }

    private fun setupObservers() {
        // 실종자 상세 정보 관찰
        viewModel.missingPersonDetail.observe(viewLifecycleOwner) { detail ->
            if (detail != null) {
                binding.missingPerson = detail
            } else {
                Toast.makeText(context, "실종자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}