package com.dotjoo.aghslilnidelivery.base

import androidx.paging.*
import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BasePagingUseCase<RequestItem : Any, Params : Any> {

    abstract fun getPagingSource(params: Params?): PagingSource<Int, RequestItem>


    fun invoke(scope: CoroutineScope, params: Params?, callBack: (PagingData<RequestItem>) -> Unit) {
        scope.launch(Dispatchers.IO) {
            execute(scope, params).collect {
                withContext(Dispatchers.Main) {
                    callBack.invoke(it)
                }
            }
        }
    }

/*
    private fun execute(scope: CoroutineScope, params: Params?): Flow<PagingData<RequestItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { getPagingSource(params) }
        ).flow.cachedIn(scope)
    }
*/

    private fun execute(scope: CoroutineScope, params: Params?): Flow<PagingData<RequestItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
            ),
            pagingSourceFactory = { getPagingSource(params) }
        ).flow.cachedIn(scope)
    }

}
