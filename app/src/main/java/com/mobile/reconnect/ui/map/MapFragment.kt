package com.mobile.reconnect.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LodLabel
import com.kakao.vectormap.shape.DotPoints
import com.kakao.vectormap.shape.Polygon
import com.kakao.vectormap.shape.PolygonOptions
import com.kakao.vectormap.shape.PolygonStyles
import com.kakao.vectormap.shape.PolygonStylesSet
import com.mobile.reconnect.R
import com.mobile.reconnect.data.model.search.MissingPerson
import com.mobile.reconnect.databinding.FragmentMapBinding
import com.mobile.reconnect.ui.common.BaseFragment
import com.mobile.reconnect.ui.map.adapter.MissingPersonsAdapter
import com.mobile.reconnect.ui.map.viewmodel.HomeBottomViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map) {
	private val viewModel: HomeBottomViewModel by activityViewModels()

	private lateinit var mapView: MapView
	private var kakaoMap: KakaoMap? = null
	private lateinit var fusedLocationClient: FusedLocationProviderClient

	private val list = mutableListOf<MissingPerson>()

	private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
	private lateinit var adapter: MissingPersonsAdapter

	private var currentCircle: Polygon? = null

	private lateinit var client: OkHttpClient
	private lateinit var stompClient: StompClient
	private var label: Label? = null
	private var lodLabel: LodLabel? = null

	private val PERMISSIONS_REQUEST_CODE = 100
	private val permissions = arrayOf(
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_COARSE_LOCATION
	)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

		setBottomSheet() // 바텀시트 설정
		showMapView() // 카카오 맵 설정
		checkPermissions() // 위치 권한 설정
		setupStompConnection() // stomp 설정

		binding.radius1km.setOnClickListener {
			drawCircle(1000.0, Color.argb(60, 248, 144, 53))
			bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
			viewModel.updateRadius(1)
		}

		binding.radius2km.setOnClickListener {
			drawCircle(2000.0, Color.argb(60, 126, 185, 253))
			bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
			viewModel.updateRadius(2)
		}

		binding.radius3km.setOnClickListener {
			drawCircle(3000.0, Color.argb(60, 137, 211, 139))
			bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
			viewModel.updateRadius(3)
		}

		binding.radius4km.setOnClickListener {
			drawCircle(4000.0, Color.argb(60, 228, 189, 255))
			bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
			viewModel.updateRadius(4)
		}

		binding.radius5km.setOnClickListener {
			drawCircle(5000.0, Color.argb(60, 253, 247, 123))
			bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
			viewModel.updateRadius(5)
		}

		viewModel.radius.observe(viewLifecycleOwner) { radiusValue ->
			val missingPersonCount = adapter.currentList.size + 1
			binding.tvTitle.text = "반경 ${radiusValue}km 이내 실종자 ${missingPersonCount}명"
		}
	}

	/***
	 * 카카오 맵
	 */
	private fun showMapView() {
		mapView = binding.map

		mapView.start(object : MapLifeCycleCallback() {
			override fun onMapDestroy() {
				Log.d("KakaoMap", "onMapDestroy")
			}

			override fun onMapError(p0: Exception?) {
				Log.e("KakaoMap", "onMapError")
			}
		}, object : KakaoMapReadyCallback() {
			override fun onMapReady(kakaomap: KakaoMap) {
				kakaoMap = kakaomap
			}
		})
	}

	private fun centerMap(latitude: Double, longitude: Double) {
		val latLng = LatLng.from(latitude, longitude)

		kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(latLng, 13))
	}

	/***
	 * bottom sheet
	 */
	private fun setBottomSheet() {
		val bottomSheet = view?.findViewById<View>(R.id.bottom_sheet)
		bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

		bottomSheet.setOnClickListener {
			if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HALF_EXPANDED) {
				bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
			} else {
				bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
			}
		}
	}

	/***
	 * 위치 권한
	 */
	private fun checkPermissions() {
		val requestList = mutableListOf<String>()
		for (permission in permissions) {
			if (ActivityCompat.checkSelfPermission(
					requireContext(),
					permission
				) != PackageManager.PERMISSION_GRANTED
			) {
				requestList.add(permission)
				Log.d("엥", "ㅜㅜㅜ")
			}
		}

		if (requestList.isNotEmpty()) {
			// 권한이 없으면 요청
			ActivityCompat.requestPermissions(
				requireActivity(),
				requestList.toTypedArray(),
				PERMISSIONS_REQUEST_CODE
			)
		} else {
			// 권한이 있으면 바로 위치 가져오기
			getLocation()
		}
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == PERMISSIONS_REQUEST_CODE) {
			for (grantResult in grantResults) {
				if (grantResult == PackageManager.PERMISSION_DENIED) {
					Toast.makeText(requireContext(), "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
					return
				}
			}
		}
	}

	@SuppressLint("MissingPermission")
	private fun getLocation() {
		// 위치 서비스가 활성화되어 있는지 확인
		val locationRequest = LocationRequest.create().apply {
			interval = 10000 // 10초마다 위치 업데이트
			fastestInterval = 5000 // 5초마다 빠른 위치 업데이트
			priority = LocationRequest.PRIORITY_HIGH_ACCURACY
		}

		val locationCallback = object : LocationCallback() {
			override fun onLocationResult(p0: LocationResult) {
				if (p0 != null) {
					super.onLocationResult(p0)
				}
				p0?.let { it ->
					val location = it.lastLocation
					location?.let {
						viewModel.setLocation(location.latitude, location.longitude)
						drawLabel(location.latitude, location.longitude)  // 현위치에 라벨 추가
						centerMap(location.latitude, location.longitude)
//						drawCircle(location.latitude, location.longitude) // 위치를 기반으로 원 그리기
					}
				}
			}
		}

		if (ActivityCompat.checkSelfPermission(
				requireContext(),
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			fusedLocationClient.requestLocationUpdates(
				locationRequest,
				locationCallback,
				Looper.getMainLooper()
			)
		} else {
			// 권한이 없으면 권한 요청
			ActivityCompat.requestPermissions(
				requireActivity(),
				arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
				PERMISSIONS_REQUEST_CODE
			)
		}
	}

	/***
	 * 현위치 label 출력
	 */
	private fun drawLabel(lat: Double, lng: Double) {
		label?.remove()

		val styles = kakaoMap!!.labelManager
			?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_my_location)))

		val options = LabelOptions.from(LatLng.from(lat, lng))
			.setStyles(styles)

		val layer = kakaoMap!!.labelManager!!.layer

		label = layer!!.addLabel(options)
//		label.show()
	}

	/***
	 * 위치 label 출력
	 */
	private fun drawLocationLabel(lat: Double, lng: Double) {
		val styles: LabelStyles = LabelStyles.from(LabelStyle.from(R.drawable.ic_predicted_location))

		val options = LabelOptions.from(LatLng.from(lat, lng))
			.setStyles(styles)

		val layer = kakaoMap!!.labelManager!!.lodLayer

		lodLabel = layer!!.addLodLabel(options)
	}

	private fun removeLocationLabel() {
		lodLabel?.let {
			kakaoMap!!.labelManager!!.lodLayer!!.remove(it)
			lodLabel = null
		}
	}

	/***
	 * stomp
	 */
	private fun setupStompConnection() {
		client = OkHttpClient()
		val url = "ws://223.130.139.166:8080/ws"
		stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

		runStompTopicRadiusMissingPersons()
	}

	/***
	 * stomp
	 */
	@SuppressLint("CheckResult")
	private fun runStompTopicRadiusMissingPersons() {
		stompClient.topic("/topic/radius-missing-persons").subscribe { topicMessage ->
//			Log.i("Message Received", topicMessage.payload)
			try {
				val jsonArray = JSONArray(topicMessage.payload)
				val personList = mutableListOf<MissingPerson>()

				for (i in 0 until jsonArray.length()) {
					val jsonObject = jsonArray.getJSONObject(i)
					val person = MissingPerson(
						id = jsonObject.getInt("id"),
						name = jsonObject.getString("name"),
						imageURL = jsonObject.getString("imageURL"),
						specialFeature = jsonObject.getString("specialFeature"),
						gender = jsonObject.getString("gender"),
						age = jsonObject.getInt("age"),
						height = jsonObject.getInt("height"),
						weight = jsonObject.getInt("weight"),
						tops = jsonObject.getString("tops"),
						bottoms = jsonObject.getString("bottoms"),
						shoes = jsonObject.getString("shoes"),
						accessories = jsonObject.getString("accessories"),
						hair = jsonObject.getString("hair")
					)
					personList.add(person)
				}

				viewModel.personListLiveData.postValue(personList)
				Log.i("STOMP Response", "Received and parsed person list")
			} catch (e: Exception) {
				Log.e("STOMP Error", "Error parsing response", e)
			}
		}

		viewModel.personListLiveData.observe(viewLifecycleOwner) { personList ->
			initProjectRecyclerView(personList)
			updateRecyclerView(personList)
		}

		val headerList = arrayListOf<StompHeader>()
		stompClient.connect(headerList)

		stompClient.lifecycle().subscribe { lifecycleEvent ->
			when (lifecycleEvent.type) {
				LifecycleEvent.Type.OPENED -> {
					Log.i("OPENED", "STOMP connection opened")
				}

				LifecycleEvent.Type.CLOSED -> {
					Log.i("CLOSED", "STOMP connection closed")
				}

				LifecycleEvent.Type.ERROR -> {
					Log.i("ERROR", "Error in STOMP connection")
					Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
				}

				else -> {
					Log.i("ELSE", lifecycleEvent.message ?: "Unknown event")
				}
			}
		}

		val combinedLocation = MediatorLiveData<Pair<Double, Double>>()

		combinedLocation.addSource(viewModel.uLatitude) { latitude ->
			val longitude = viewModel.uLongitude.value ?: 0.0
			combinedLocation.value = Pair(latitude, longitude)
		}

		combinedLocation.addSource(viewModel.uLongitude) { longitude ->
			val latitude = viewModel.uLatitude.value ?: 0.0
			combinedLocation.value = Pair(latitude, longitude)
		}

		combinedLocation.observe(viewLifecycleOwner) { (latitude, longitude) ->
			val data = JSONObject().apply {
				put("userLocation", JSONObject().apply {
					put("latitude", latitude)
					put("longitude", longitude)
				})
				put("radius", viewModel.radius.value)
			}

			stompClient.send("/app/ws/radius/missing-persons", data.toString()).subscribe()
		}
	}

	@SuppressLint("CheckResult")
	private fun runStompTopicPredictedLocations(id: Int) {
		stompClient.topic("/topic/predicted-locations").subscribe { topicMessage ->
			Log.i("Message Received", topicMessage.payload)
			try {
				// JSONArray로 변환
				val jsonArray = JSONArray(topicMessage.payload)

				// 각 위치 데이터를 처리
				for (i in 0 until jsonArray.length()) {
					val locationObject = jsonArray.getJSONObject(i)

					// 위치 정보 추출
					val latitude = locationObject?.optDouble("latitude", 0.0)
					val longitude = locationObject?.optDouble("longitude", 0.0)

					drawLocationLabel(latitude!!, longitude!!)
				}
			} catch (e: JSONException) {
				e.printStackTrace()
				Log.e("JSON Error", "Failed to parse predicted locations: ${e.message}")
			}
		}

		val headerList = arrayListOf<StompHeader>()
		stompClient.connect(headerList)

		stompClient.lifecycle().subscribe { lifecycleEvent ->
			when (lifecycleEvent.type) {
				LifecycleEvent.Type.OPENED -> {
					Log.i("OPENED", "STOMP connection opened")
				}

				LifecycleEvent.Type.CLOSED -> {
					Log.i("CLOSED", "STOMP connection closed")
				}

				LifecycleEvent.Type.ERROR -> {
					Log.i("ERROR", "Error in STOMP connection")
					Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
				}

				else -> {
					Log.i("ELSE", lifecycleEvent.message ?: "Unknown event")
				}
			}
		}

		val combinedLocation = MediatorLiveData<Pair<Double, Double>>()

		combinedLocation.addSource(viewModel.uLatitude) { latitude ->
			val longitude = viewModel.uLongitude.value ?: 0.0
			combinedLocation.value = Pair(latitude, longitude)
		}

		combinedLocation.addSource(viewModel.uLongitude) { longitude ->
			val latitude = viewModel.uLatitude.value ?: 0.0
			combinedLocation.value = Pair(latitude, longitude)
		}

		combinedLocation.observe(viewLifecycleOwner) { (latitude, longitude) ->
			val data = JSONObject().apply {
				put("userLocation", JSONObject().apply {
					put("latitude", latitude)
					put("longitude", longitude)
				})
				put("radius", viewModel.radius.value)
				put("missedUserSeq", id)
			}

			stompClient.send("/app/ws/location", data.toString()).subscribe()
		}
	}

	companion object {
		private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
	}

	private fun initProjectRecyclerView(personList: List<MissingPerson>) {
		adapter = MissingPersonsAdapter { id ->
			showItemDetails(id)
			viewModel.setId(id)
		}
		binding.rvMissingPersonsList.adapter = adapter
		binding.rvMissingPersonsList.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

		adapter.submitList(personList)
	}

	private fun updateRecyclerView(newData: List<MissingPerson>) {
		list.clear() // 기존 데이터 클리어
		list.addAll(newData) // 새로운 데이터 추가
		binding.rvMissingPersonsList.adapter?.notifyDataSetChanged()
	}

	private fun showItemDetails(id: Int) {
		println("클릭된 아이템 ID: $id")
		runStompTopicPredictedLocations(id)
		removeLocationLabel()
	}

	/***
	 * 반경 원형 그리기
	 */
	@SuppressLint("MissingPermission")
	private fun drawCircle(radius: Double, color: Int) {
		// 기존 원 삭제
		currentCircle?.remove()

		fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
			if (location != null) {
				val currentLatLng = LatLng.from(location.latitude, location.longitude)

				// 반지름 200 미터(meter) 의 원형 폴리곤
				val circleOptions: PolygonOptions = PolygonOptions.from(
					DotPoints.fromCircle(
						currentLatLng,
						radius.toFloat()
					)
				)
					.setStylesSet(PolygonStylesSet.from(PolygonStyles.from(color)))


				currentCircle = kakaoMap!!.shapeManager!!.layer.addPolygon(circleOptions)
			}
		}
	}
}