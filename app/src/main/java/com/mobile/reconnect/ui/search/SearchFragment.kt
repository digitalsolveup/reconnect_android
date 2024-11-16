package com.mobile.reconnect.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.gson.GsonBuilder
import com.mobile.reconnect.R
import com.mobile.reconnect.data.model.search.SearchRequest
import com.mobile.reconnect.databinding.FragmentSearchBinding
import com.mobile.reconnect.ui.common.BaseFragment
import com.mobile.reconnect.ui.search.viewmodel.SearchViewModel
import com.mobile.reconnect.ui.map.adapter.MissingPersonsAdapter
import com.mobile.reconnect.ui.search.adapter.SearchMissingPersonsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
	private val viewModel: SearchViewModel by activityViewModels()

	private lateinit var adapter: SearchMissingPersonsAdapter
	private var request = SearchRequest()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupRecyclerView()
		viewModel.searchMissingPersons(request) // 실종자 목록 조회

		// 검색 뷰 설정
		binding.searchView.setOnQueryTextListener(object :
			androidx.appcompat.widget.SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				query?.let {
					request = SearchRequest(name = it)
					viewModel.searchMissingPersons(request)
				}
				return true
			}

			override fun onQueryTextChange(newText: String?): Boolean {
				return false
			}
		})

		// 필터링 버튼 클릭 리스너
		binding.filteringChipAll.setOnClickListener {
			val filterBottomSheet = SearchFilteringFragment()
			filterBottomSheet.show(childFragmentManager, filterBottomSheet.tag)
		}

		binding.filteringChipGender.setOnClickListener {
			if (binding.filteringChipGender.chipStrokeColor != ContextCompat.getColorStateList(
					requireContext(),
					R.color.primary_red
				)
			) {
				val filterBottomSheet = SearchFilteringGenderFragment()
				filterBottomSheet.show(childFragmentManager, filterBottomSheet.tag)
			}

			setChipClickListener(binding.filteringChipGender, R.color.primary_red, R.color.gray_300)
		}

		binding.filteringChipFeature.setOnClickListener {
			if (binding.filteringChipGender.chipStrokeColor != ContextCompat.getColorStateList(
					requireContext(),
					R.color.primary_red
				)
			) {
				val filterBottomSheet = SearchFilteringFeatureFragment()
				filterBottomSheet.show(childFragmentManager, filterBottomSheet.tag)
			}

			setChipClickListener(
				binding.filteringChipFeature,
				R.color.primary_red,
				R.color.gray_300
			)
		}

		binding.filteringChipAge.setOnClickListener {
			if (binding.filteringChipGender.chipStrokeColor != ContextCompat.getColorStateList(
					requireContext(),
					R.color.primary_red
				)
			) {
				val filterBottomSheet = SearchFilteringAgeFragment()
				filterBottomSheet.show(childFragmentManager, filterBottomSheet.tag)
			}
			setChipClickListener(binding.filteringChipAge, R.color.primary_red, R.color.gray_300)
		}

		// ViewModel 상태에 따라 UI 업데이트
		viewModel.missingPersons.observe(viewLifecycleOwner) { persons ->
			adapter.submitList(persons)
		}

		// 에러 처리
		viewModel.error.observe(viewLifecycleOwner) {

		}
	}

	private fun setupRecyclerView() {
		adapter = SearchMissingPersonsAdapter { person ->
			Log.d("SearchFragment", "Clicked: ${person.name}")
		}

		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerView.adapter = adapter
	}

	// Chip 클릭 시 색상 변경 함수
	private fun setChipClickListener(chip: Chip, selectedColor: Int, unselectedColor: Int) {
		val currentStrokeColor = chip.chipStrokeColor?.defaultColor

		if (currentStrokeColor == ContextCompat.getColor(requireContext(), selectedColor)) {
			chip.setChipStrokeColorResource(unselectedColor)
			chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_600))

			viewModel.searchMissingPersons(request) // 실종자 목록 조회
		} else {
			chip.setChipStrokeColorResource(selectedColor)
			chip.setTextColor(ContextCompat.getColor(requireContext(), selectedColor))
		}

		chip.isChecked = !chip.isChecked
	}
}
