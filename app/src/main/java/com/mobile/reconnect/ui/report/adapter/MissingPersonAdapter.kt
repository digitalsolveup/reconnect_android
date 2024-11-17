package com.mobile.reconnect.ui.report.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobile.reconnect.data.model.report.MissingPersonListResponse
import com.mobile.reconnect.databinding.ItemReportMissingPersonBinding

class MissingPersonAdapter(
	private val onClick: (MissingPersonListResponse, View) -> Unit
) : ListAdapter<MissingPersonListResponse, MissingPersonAdapter.ViewHolder>(DiffCallback) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding = ItemReportMissingPersonBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = getItem(position)
		holder.bind(item, onClick)
	}

	class ViewHolder(private val binding: ItemReportMissingPersonBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: MissingPersonListResponse, onClick: (MissingPersonListResponse, View) -> Unit) {
			binding.missingPerson = item
			binding.root.setOnClickListener { onClick(item, it) }
		}
	}

	companion object DiffCallback : DiffUtil.ItemCallback<MissingPersonListResponse>() {
		override fun areItemsTheSame(
			oldItem: MissingPersonListResponse,
			newItem: MissingPersonListResponse
		): Boolean = oldItem.id == newItem.id

		override fun areContentsTheSame(
			oldItem: MissingPersonListResponse,
			newItem: MissingPersonListResponse
		): Boolean = oldItem == newItem
	}
}