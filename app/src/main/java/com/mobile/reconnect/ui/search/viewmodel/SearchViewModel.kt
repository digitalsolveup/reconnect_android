package com.mobile.reconnect.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.reconnect.data.model.search.MissingPerson
import com.mobile.reconnect.data.model.search.PageableRequest
import com.mobile.reconnect.data.model.search.SearchRequest
import com.mobile.reconnect.data.model.search.SearchResponse
import com.mobile.reconnect.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchRepository: SearchRepository
) : ViewModel() {

	private val _missingPersons = MutableLiveData<
			List<MissingPerson>?>()
	val missingPersons: MutableLiveData<
			List<MissingPerson>?>
		get() = _missingPersons

	private val _ifFiltered = MutableLiveData<Boolean>()
	val ifFiltered: LiveData<Boolean> get() = _ifFiltered

	private val _error = MutableLiveData<String>()
	val error: LiveData<String> get() = _error

	private var currentPage = 0

	/***
	 * 실종자 검색
	 * @return 실종자 리스트
	 */
	fun searchMissingPersons(query: SearchRequest) {
		val pageable = PageableRequest(currentPage, 10)
		Log.d("SearchViewModel", "Searching for: $query with page: $currentPage")

		searchRepository.searchMissingPerson(query, pageable).enqueue(object :
			Callback<SearchResponse> {
			override fun onResponse(
				call: Call<SearchResponse>,
				response: Response<SearchResponse>
			) {
				if (response.isSuccessful) {
					// 성공적인 응답 처리
					val responseBody = response.body()
					if (responseBody != null && responseBody.content.isNotEmpty()) {

						// 필터링
						var filteredList = responseBody.content.filter { missingPerson ->
							missingPerson.name.equals(query.name, ignoreCase = true) ||
									missingPerson.gender == query.gender ||
									missingPerson.age.toString() == query.age.toString() ||
									missingPerson.specialFeature == query.specialFeature
						}

						// 필터링된 리스트를 _missingPersons에 업데이트
						if (filteredList.isNotEmpty()) {
							_missingPersons.value = filteredList
							currentPage++
							Log.d("SearchViewModel", "성공: ${filteredList.size}개 결과 찾음")
						} else {
							Log.d("SearchViewModel", "검색 결과 없음: $query")
							_missingPersons.value = emptyList()
						}
					} else {
						Log.d("SearchViewModel", "데이터 없음")
						_missingPersons.value = emptyList()
					}
				} else {
					// 실패 시 처리
					Log.e(
						"SearchViewModel",
						"오류: ${response.code()} - ${response.message()} - ${response.body()}"
					)
					_error.value = "검색 결과를 가져오지 못했습니다."
				}
			}

			override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
				// 네트워크 오류 처리
				Log.e("SearchViewModel", "Network error: ${t.localizedMessage}")
				_error.value = "네트워크 오류가 발생했습니다."
			}
		})
	}

	fun searchMissingPersonsFiltering(query: SearchRequest) {
		val pageable = PageableRequest(currentPage, 10)
		Log.d("SearchViewModel", "Searching for: $query with page: $currentPage")

		searchRepository.searchMissingPerson(query, pageable).enqueue(object :
			Callback<SearchResponse> {
			override fun onResponse(
				call: Call<SearchResponse>,
				response: Response<SearchResponse>
			) {
				if (response.isSuccessful) {
					// 성공적인 응답 처리
					val responseBody = response.body()
					if (responseBody != null && responseBody.content.isNotEmpty()) {

						// 필터링
						var filteredList = responseBody.content.filter { missingPerson ->
							missingPerson.name.equals(query.name, ignoreCase = true) &&
									missingPerson.gender == query.gender &&
									missingPerson.age.toString() == query.age.toString() &&
									missingPerson.specialFeature == query.specialFeature
						}

						// 필터링된 리스트를 _missingPersons에 업데이트
						if (filteredList.isNotEmpty()) {
							_missingPersons.value = filteredList
							currentPage++
							Log.d("SearchViewModel", "성공: ${filteredList.size}개 결과 찾음")
						} else {
							Log.d("SearchViewModel", "검색 결과 없음: $query")
							_missingPersons.value = emptyList()
						}
					} else {
						Log.d("SearchViewModel", "데이터 없음")
						_missingPersons.value = emptyList()
					}
				} else {
					// 실패 시 처리
					Log.e(
						"SearchViewModel",
						"오류: ${response.code()} - ${response.message()} - ${response.body()}"
					)
					_error.value = "검색 결과를 가져오지 못했습니다."
				}
			}

			override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
				// 네트워크 오류 처리
				Log.e("SearchViewModel", "Network error: ${t.localizedMessage}")
				_error.value = "네트워크 오류가 발생했습니다."
			}
		})
	}

	fun setFilters(boolean: Boolean) {
		_ifFiltered.value = boolean
	}
}
