package com.dotjoo.aghslilnidelivery.base

object SearchParamType {
    val Home_Search = 1
    val Search_Laundries = 2

}

open class PagingParams : HashMapParams {
    open var page: Int = 1
    override fun dataClass(): Any {
        return 0
    }
}

data class AllLaundriesParams(
    val lat: String, val lng: String,
) : PagingParams() {
    override fun dataClass(): Any = this
    override var page: Int = INITIAL_PAGE_INDEX
}


data class FilterAllLaundriesParams(
    val lat: String, val lng: String,
    val additional_service: ArrayList<String>?,
    val services: ArrayList<String>?,
    val urgent: Boolean?,
    val top_rated: Boolean?,
    val sort_alphapitic: Boolean?,
    val top_rate_sort: Boolean?,

    ) : PagingParams() {
    override fun dataClass(): Any = this
    override var page: Int = INITIAL_PAGE_INDEX
}


data class SearchParams(
    val search_key: String, val lat: String, val lng: String, val type: Int
) : PagingParams() {
    override fun dataClass(): Any = this
    override var page: Int = INITIAL_PAGE_INDEX
}

interface HashMapParams {
    fun dataClass(): Any
}

