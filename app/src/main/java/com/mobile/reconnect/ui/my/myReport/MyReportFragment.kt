package com.mobile.reconnect.ui.my.myReport

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.reconnect.R
import com.mobile.reconnect.data.model.MissingPerson_ex
import com.mobile.reconnect.data.model.MyReportList
import com.mobile.reconnect.data.model.enumeration.Status
import com.mobile.reconnect.databinding.FragmentMyReportBinding
import com.mobile.reconnect.ui.my.viewmodel.MyViewModel
import com.mobile.reconnect.ui.common.BaseFragment
import com.mobile.reconnect.ui.map.adapter.MissingPersonsAdapter
import com.mobile.reconnect.ui.my.adapter.MyReportAdapter

class MyReportFragment: BaseFragment<FragmentMyReportBinding>(R.layout.fragment_my_report) {
	private val viewModel: MyViewModel by viewModels()
	private lateinit var reports: List<MyReportList>
	private lateinit var adapter: MyReportAdapter

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupRecyclerView()
	}
	private fun setupRecyclerView() {
		reports = listOf(
			MyReportList(R.drawable.ic_baseline_my_24, "홍길동", Status.SEARCHING, "2024-11-10"),
			MyReportList(R.drawable.ic_baseline_my_24, "박길동", Status.FIND, "2024-11-10"),
			MyReportList(R.drawable.ic_baseline_my_24, "이길동", Status.FIND, "2024-11-10"),
			MyReportList(R.drawable.ic_baseline_my_24, "김길동", Status.SEARCHING, "2024-11-10"),
		)

		adapter = MyReportAdapter(reports) { person ->
			Log.d("HomeBottomSheetFragment", "Clicked: ${person.name}")
		}

		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerView.adapter = adapter
	}

}