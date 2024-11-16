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
import com.mobile.reconnect.data.model.search.SearchRequest
import com.mobile.reconnect.ui.search.viewmodel.SearchViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFilteringFragment : BottomSheetDialogFragment() {
	private var _binding: FragmentSearchFilteringBinding? = null
	private val binding get() = _binding!!

	private val viewModel: SearchViewModel by activityViewModels()
	private var request = SearchRequest()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSearchFilteringBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		var age = ""
		var gender = ""
		var feature = ""

		binding.btnMale.setOnClickListener {
			selectGender(binding.btnMale, binding.btnFemale)
			gender = "MALE"
		}

		binding.btnFemale.setOnClickListener {
			selectGender(binding.btnFemale, binding.btnMale)
			gender = "FEMALE"
		}

		binding.etAge.doAfterTextChanged {
			age = binding.etAge.text.toString()
		}

		binding.btnNonDisabled.setOnClickListener {
			setChipClickListener(binding.btnNonDisabled, R.color.primary_red, R.color.gray_300)
			feature = "NONE"
		}

		binding.btnDisabled.setOnClickListener {
			setChipClickListener(binding.btnDisabled, R.color.primary_red, R.color.gray_300)
			feature = "DISABILITY"
		}

		binding.btnDementia.setOnClickListener {
			setChipClickListener(binding.btnDementia, R.color.primary_red, R.color.gray_300)
			feature = "DEMENTIA"
		}

		binding.btnRunaway.setOnClickListener {
			setChipClickListener(binding.btnRunaway, R.color.primary_red, R.color.gray_300)
			feature = "RUNAWAY"
		}

		binding.btnEtc.setOnClickListener {
			setChipClickListener(binding.btnEtc, R.color.primary_red, R.color.gray_300)
			feature = "OTHER"
		}

		binding.btnCancel.setOnClickListener {
			dismiss()
		}

		binding.btnSearch.setOnClickListener {
			viewModel.searchMissingPersonsFiltering(
				SearchRequest(
					gender = gender,
					age = age.toInt(),
					specialFeature = feature
				)
			)
		}
	}

	private fun selectGender(selectedChip: Chip, unselectedChip: Chip) {
		val selectedColor = ContextCompat.getColor(requireContext(), R.color.primary_red)

		val currentStrokeColor = selectedChip.chipStrokeColor?.defaultColor

		if (currentStrokeColor == selectedColor) {
			selectedChip.setChipStrokeColorResource(R.color.gray_300)
			selectedChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))
			selectedChip.isChecked = false
		} else {
			selectedChip.setChipStrokeColorResource(R.color.primary_red)
			selectedChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_red))
			selectedChip.isChecked = true
		}

		unselectedChip.setChipStrokeColorResource(R.color.gray_300)
		unselectedChip.isChecked = false
	}

	// Chip 클릭 시 색상 변경 함수
	private fun setChipClickListener(chip: Chip, selectedColor: Int, unselectedColor: Int) {
		chip.setOnClickListener {
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
