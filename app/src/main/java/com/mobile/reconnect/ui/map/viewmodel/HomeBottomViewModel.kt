package com.mobile.reconnect.ui.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.vectormap.label.LodLabel
import com.mobile.reconnect.data.model.search.MissingPerson

class HomeBottomViewModel : ViewModel() {
	private val _radius = MutableLiveData<Int>()
	val radius: LiveData<Int> get() = _radius

	private val _id = MutableLiveData<Int>()
	val id: LiveData<Int> get() = _id

	private val _uLatitude = MutableLiveData<Double>()
	val uLatitude: LiveData<Double> get() = _uLatitude

	private val _uLongitude = MutableLiveData<Double>()
	val uLongitude: LiveData<Double> get() = _uLongitude

	val personListLiveData = MutableLiveData<List<MissingPerson>>()

	private var _lodLabel = MutableLiveData<LodLabel?>()
	val lodLabel: LiveData<LodLabel?> get() = _lodLabel

	fun updateRadius(radiusValue: Int) {
		_radius.value = radiusValue
	}

	fun setLocation(latitude: Double, longitude: Double) {
		_uLatitude.value = latitude
		_uLongitude.value = longitude
	}

	fun setId(id: Int) {
		_id.value = id
	}

	fun setLodLabel(lodLabel: LodLabel) {
		_lodLabel.value = lodLabel
	}

	fun resetLodLabel(lodLabel: LodLabel) {
		_lodLabel.value = null
	}
}