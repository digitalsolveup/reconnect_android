package com.mobile.reconnect.ui.report

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mobile.reconnect.R
import com.mobile.reconnect.databinding.FragmentReportRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportRegistrationFragment : Fragment(R.layout.fragment_report_registration) {

    private var _binding: FragmentReportRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReportRegistrationBinding.bind(view)


        // reportbtn 클릭 시 MyReportFragment로 이동
        binding.reportbtn.setOnClickListener {
            findNavController().navigate(R.id.action_reportRegistrationFragment_to_myReportFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}