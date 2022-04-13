package com.sample.marketprices.network

import com.sample.marketprices.models.MarketResponse
import com.sample.marketprices.utils.Constants.Companion.API_KEY
import com.sample.marketprices.utils.Constants.Companion.GET_COMMODITIES
import com.sample.marketprices.utils.Constants.Companion.QUERY_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Kailash Suthar on 11/04/2022.
 */
interface ApiService {

    @GET(GET_COMMODITIES)
    suspend fun searchMarket(
        @Query("api-key") term: String = API_KEY,
        @Query("format") format: String = "json",
        @Query("filters[district]") district: String?,
        @Query("filters[market]") market: String?,
        @Query("limit") limit: Int = QUERY_PAGE_SIZE,
        @Query("offset") offset: Int?
    ): Response<MarketResponse>

}