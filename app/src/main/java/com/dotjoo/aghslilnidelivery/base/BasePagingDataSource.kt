package com.dotjoo.aghslilnidelivery.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
 import retrofit2.HttpException
import java.io.IOException

const val INITIAL_PAGE_INDEX = 1


abstract class BasePagingDataSource<ResponseItem : Any> :
    PagingSource<Int, ResponseItem>() {


    abstract suspend fun execute(): NetworkResponse<BasePagingResponse<ResponseItem>, ErrorResponse>
    abstract val queryParams: PagingParams

    open fun onResponseReceived(data: BasePagingResponse<ResponseItem>?) {}

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseItem> {

        return try {
            val current: Int = params.key ?: INITIAL_PAGE_INDEX
            queryParams.page = current
            var response = execute()
            when (response) {
                is NetworkResponse.Success -> {
                    onResponseReceived(response.body)
                    var total: Int? =  (response.body.data?.total?.div(response.body.data?.perPage!!))
                    if (total != null) {
                        total=total+1
                    }


                    val nextKey =
                        if (response.body.data?.data?.isEmpty() == true || current >= total!!) {
                            null
                        } else {
                            // initial load size = 3 * NETWORK_PAGE_SIZE
                            // ensure we're not requesting duplicating items, at the 2nd request
                            current.plus(1)
                        }
                    val prevKey = if (current == INITIAL_PAGE_INDEX) {
                        null
                    } else {
                        current.minus(1)
                    }

                    val listOfData = response.body.data?.data?.filterNotNull()
                    LoadResult.Page(
                        data = listOfData?: arrayListOf(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                is NetworkResponse.NetworkError -> {LoadResult.Error(Throwable(response.error.message))
                 //   LoadResult.Error(response.error)
                }
                is NetworkResponse.ServerError -> LoadResult.Error(Throwable(response.body?.Error.toString()))
                is NetworkResponse.UnknownError -> LoadResult.Error(response.error)
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, ResponseItem>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition
        /*state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }*/

    }
}