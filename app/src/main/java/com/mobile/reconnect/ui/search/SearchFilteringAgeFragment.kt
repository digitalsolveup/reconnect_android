package com.mobile.reconnect.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobile.reconnect.R
import com.mobile.reconnect.databinding.FragmentSearchFilteringBinding
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mobile.reconnect.data.model.search.SearchRequest
import com.mobile.reconnect.databinding.FragmentSearchFilteringAgeBinding
import com.mobile.reconnect.databinding.FragmentSearchFilteringGenderBinding
import com.mobile.reconnect.ui.search.viewmodel.SearchViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFilteringAgeFragment : BottomSheetDialogFragment() {
	private var _binding: FragmentSearchFilteringAgeBinding? = null
	private val binding get() = _binding!!

	private val viewModel: SearchViewModel by activityViewModels()
	private var request = SearchRequest()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSearchFilteringAgeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		var age = ""

		binding.etAge.doAfterTextChanged {
			request = SearchRequest(age = it.toString().toInt())
		}

		binding.btnSelect.setOnClickListener {
			viewModel.searchMissingPersons(request)
			dismiss()
		}

		binding.btnCancel.setOnClickListener {
			viewModel.setFilters(false)
			dismiss()
		}
	}

	private fun selectGender(selectedChip: Chip, unselectedChip: Chip) {
		val selectedColor = ContextCompat.getColor(requireContext(), R.color.primary_red)

		val currentStrokeColor = selectedChip.chipStrokeColor?.defaultColor

		if (currentStrokeColor == selectedColor) {
			selectedChip.setChipStrokeColorResource(R.color.gray_300)
			selectedChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_600))
			selectedChip.isChecked = false
		} else {
			selectedChip.setChipStrokeColorResource(R.color.primary_red)
			selectedChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_red))
			selectedChip.isChecked = true
			viewModel.setFilters(true)
		}

		unselectedChip.setChipStrokeColorResource(R.color.gray_300)
		unselectedChip.isChecked = false
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
