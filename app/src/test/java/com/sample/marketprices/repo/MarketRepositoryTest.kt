package com.sample.marketprices.repo

import com.sample.marketprices.models.Market
import com.sample.marketprices.models.MarketResponse
import com.sample.marketprices.network.ApiService
import com.sample.marketprices.utils.Constants
import org.junit.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class MarketRepositoryTest {

    lateinit var mainRepository: MarketRepository

    @Mock
    lateinit var apiService: ApiService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mainRepository = MarketRepository(apiService)
    }

    @Test
    fun `get all market prices test`() {
        val searchText = "chittor"
        val searchMText = "chittoor"
        runBlocking {
            Mockito.`when`(apiService.searchMarket(
                term = Constants.API_KEY,
                limit = Constants.QUERY_PAGE_SIZE,
                district = searchText,
                market = searchMText,
                offset = 0)).thenReturn(Response.success(MarketResponse(mutableListOf(), 0,"null")))
            val response = mainRepository.getAllMarket(
                district = searchText,
                market = searchMText,
                pageNumber = 1)
            assertEquals(listOf<Market>(), response.data)
        }

    }

}