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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mobile.reconnect.data.model.search.SearchRequest
import com.mobile.reconnect.databinding.FragmentSearchFilteringFeatureBinding
import com.mobile.reconnect.databinding.FragmentSearchFilteringGenderBinding
import com.mobile.reconnect.ui.search.viewmodel.SearchViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFilteringFeatureFragment : BottomSheetDialogFragment() {
	private var _binding: FragmentSearchFilteringFeatureBinding? = null
	private val binding get() = _binding!!

	private val viewModel: SearchViewModel by activityViewModels()
	private var request = SearchRequest()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSearchFilteringFeatureBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		var feature = ""

		binding.btnNonDisabled.setOnClickListener {
			setChipClickListener(binding.btnNonDisabled, R.color.primary_red, R.color.gray_300)
			feature = "NON_DISABLED_CHILD"
			request = SearchRequest(specialFeature = feature)
		}

		binding.btnDisabled.setOnClickListener {
			setChipClickListener(binding.btnDisabled, R.color.primary_red, R.color.gray_300)
			feature = "DISABILITY"
			request = SearchRequest(specialFeature = feature)
		}

		binding.btnDementia.setOnClickListener {
			setChipClickListener(binding.btnDementia, R.color.primary_red, R.color.gray_300)
			feature = "DEMENTIA"
			request = SearchRequest(specialFeature = feature)
		}

		binding.btnRunaway.setOnClickListener {
			setChipClickListener(binding.btnRunaway, R.color.primary_red, R.color.gray_300)
			feature = "RUNAWAY"
			request = SearchRequest(specialFeature = feature)
		}

		binding.btnEtc.setOnClickListener {
			setChipClickListener(binding.btnEtc, R.color.primary_red, R.color.gray_300)
			feature = "OTHER"
			request = SearchRequest(specialFeature = feature)
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


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun setChipClickListener(chip: Chip, selectedColor: Int, unselectedColor: Int) {
		val currentStrokeColor = chip.chipStrokeColor?.defaultColor

		if (currentStrokeColor == ContextCompat.getColor(requireContext(), selectedColor)) {
			chip.setChipStrokeColorResource(unselectedColor)
			chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))
		} else {
			chip.setChipStrokeColorResource(selectedColor)
			chip.setTextColor(ContextCompat.getColor(requireContext(), selectedColor))
		}

		chip.isChecked = !chip.isChecked
	}
}
