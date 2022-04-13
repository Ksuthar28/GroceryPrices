package com.sample.marketprices.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.marketprices.getOrAwaitValue
import com.sample.marketprices.models.Market
import com.sample.marketprices.network.ApiService
import com.sample.marketprices.repo.MarketRepository
import com.sample.marketprices.utils.Resource
import org.junit.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MarketViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    lateinit var mainViewModel: MarketViewModel
    lateinit var mainRepository: MarketRepository

    @Mock
    lateinit var apiService: ApiService

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        mainRepository = MarketRepository(apiService)
        mainViewModel = MarketViewModel(mainRepository)
    }

    @Test
    fun getAllMarketsTest() {
        runBlocking {
            Mockito.`when`(
                mainRepository.getAllMarket(
                    district = null,
                    market = null,
                    pageNumber = 1
                )
            )
                .thenReturn(
                    Resource.Success(
                        mutableListOf(
                            Market(
                                state = "Andhra Pradesh",
                                district = "Chittor",
                                market = "Chittoor",
                                commodity = "Gur(Jaggery)",
                                variety = "NO 2",
                                arrivalDate = "11/04/2022",
                                minPrice = "3000",
                                maxPrice = "3000",
                                modalPrice = "3000"
                            )
                        )
                    )
                )
            mainViewModel.getMarkets(
                district = null,
                market = null
            )
            val result = mainViewModel.markets.getOrAwaitValue()
            assertEquals(
                listOf(
                    Market(
                        state = "Andhra Pradesh",
                        district = "Chittor",
                        market = "Chittoor",
                        commodity = "Gur(Jaggery)",
                        variety = "NO 2",
                        arrivalDate = "11/04/2022",
                        minPrice = "3000",
                        maxPrice = "3000",
                        modalPrice = "3000"
                    )
                ), result
            )
        }
    }


    @Test
    fun `empty market list test`() {
        runBlocking {
            Mockito.`when`(
                mainRepository.getAllMarket(
                    district = null,
                    market = null,
                    pageNumber = 1
                )
            )
                .thenReturn(Resource.Success(mutableListOf()))
            mainViewModel.getMarkets(
                district = null,
                market = null
            )
            val result = mainViewModel.markets.getOrAwaitValue()
            assertEquals(listOf<Market>(), result)
        }
    }

}