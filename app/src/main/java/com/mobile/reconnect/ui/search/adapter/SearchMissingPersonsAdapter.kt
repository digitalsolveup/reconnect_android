package com.mobile.reconnect.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobile.reconnect.data.model.search.MissingPerson
import com.mobile.reconnect.databinding.ItemMissingPersonBinding
import com.mobile.reconnect.ui.map.adapter.MissingPersonDiffCallback
import com.mobile.reconnect.ui.map.adapter.MissingPersonsAdapter
import com.mobile.reconnect.utils.BindingAdapters.loadImage

class SearchMissingPersonsAdapter(
	private val onItemClicked: (MissingPerson) -> Unit
) : ListAdapter<MissingPerson, SearchMissingPersonsAdapter.MissingPersonViewHolder>(
	MissingPersonDiffCallback()
) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissingPersonViewHolder {
		val binding =
			ItemMissingPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return MissingPersonViewHolder(binding)
	}

	override fun onBindViewHolder(holder: MissingPersonViewHolder, position: Int) {
		val person = getItem(position)
		holder.bind(person)

		holder.itemView.setOnClickListener {
			onItemClicked(person)
		}
	}

	class MissingPersonViewHolder(private val binding: ItemMissingPersonBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(person: MissingPerson) {
			loadImage(binding.imvPerson, person.imageURL)

			val specialFeatureText = when (person.specialFeature) {
				"NONE" -> ""
				"NON_DISABLED_CHILD" -> ", 비장애아동"
				"DISABILITY" -> ", 장애"
				"DEMENTIA" -> ", 치매"
				"RUNAWAY" -> ", 가출인"
				"OTHER" -> ", 기타"
				else -> person.specialFeature
			}

			val gender = when (person.gender) {
				"MALE" -> "남"
				"FEMALE" -> "여"
				else -> "기타"
			}

			binding.tvPersonName.text = person.name
			binding.tvPersonInfo.text =
				"(${gender}, ${person.age}세, ${person.height}cm, ${person.weight}kg${specialFeatureText})"
			binding.tvPersonDetail.text =
				"${person.tops}, ${person.bottoms}, ${person.shoes}, ${person.accessories}, ${person.hair}"
		}
	}
}

class MissingPersonDiffCallback : DiffUtil.ItemCallback<MissingPerson>() {
	override fun areItemsTheSame(oldItem: MissingPerson, newItem: MissingPerson): Boolean {
		return oldItem.id == newItem.id // id로 비교
	}

	override fun areContentsTheSame(oldItem: MissingPerson, newItem: MissingPerson): Boolean {
		return oldItem == newItem // 전체 내용이 동일한지 비교
	}
}
