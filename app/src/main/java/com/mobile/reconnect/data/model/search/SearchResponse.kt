package com.mobile.reconnect.data.model.search

data class SearchResponse(
	val content: List<MissingPerson>,
	val pageable: Pageable,
	val last: Boolean,
	val totalPages: Int,
	val totalElements: Int,
	val size: Int,
	val number: Int,
	val sort: List<Sort>,
	val first: Boolean,
	val numberOfElements: Int,
	val empty: Boolean
)

data class MissingPerson(
	val id: Int,
	val name: String,
	val imageURL: String,
	val specialFeature: String,
	val gender: String,
	val age: Int,
	val height: Int,
	val weight: Int,
	val tops: String,
	val bottoms: String,
	val shoes: String,
	val accessories: String,
	val hair: String,
//	val location: String
)

data class Pageable(
	val pageNumber: Int,
	val pageSize: Int,
	val sort: List<Sort>,
	val offset: Int,
	val paged: Boolean,
	val unpaged: Boolean
)

data class Sort(
	val direction: String,
	val property: String,
	val ignoreCase: Boolean,
	val nullHandling: String,
	val ascending: Boolean,
	val descending: Boolean
)
