package com.sample.marketprices.repo

import com.sample.marketprices.network.ApiService
import com.sample.marketprices.models.Market
import com.sample.marketprices.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.sample.marketprices.utils.Resource
import javax.inject.Inject

class MarketRepository @Inject constructor(private val retrofitService: ApiService) {

    var apiResponse: MutableList<Market>? = null
    suspend fun getAllMarket(
        district: String?,
        market: String?,
        pageNumber: Int?
    ): Resource<MutableList<Market>> {
        val response = retrofitService.searchMarket(
            district = district,
            market = market,
            offset = if (pageNumber != null) (if (pageNumber == 1) 1 else (pageNumber * QUERY_PAGE_SIZE) + 1) else 0
        )
        if (response.isSuccessful) {
            response.body()?.let {
                if (apiResponse == null || pageNumber == 1) {
                    apiResponse = it.result
                } else {
                    val oldData = apiResponse
                    oldData?.addAll(it.result)
                }
                return Resource.Success((apiResponse ?: it.result))
            }
        }
        return Resource.Error(response.message())
    }

}