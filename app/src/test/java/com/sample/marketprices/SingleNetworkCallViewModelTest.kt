package com.sample.marketprices

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sample.marketprices.models.Market
import com.sample.marketprices.network.ApiService
import com.sample.marketprices.repo.MarketRepository
import com.sample.marketprices.utils.Constants.Companion.API_KEY
import com.sample.marketprices.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.sample.marketprices.utils.Resource
import com.sample.marketprices.viewmodel.MarketViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SingleNetworkCallViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var apiHelper: ApiService

    @Mock
    private lateinit var viewModel: MarketViewModel

    @Mock
    private lateinit var repository: MarketRepository

    @Mock
    private lateinit var apiUsersObserver: Observer<Resource<MutableList<Market>>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = MarketRepository(apiHelper)
        viewModel = MarketViewModel(repository)
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnFilteredSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(emptyList<Market>())
                .`when`(apiHelper)
                .searchMarket(
                    term = API_KEY,
                    limit = QUERY_PAGE_SIZE,
                    district = "chittor",
                    market = "chittoor",
                    offset = 0
                )
            viewModel.markets.observeForever(apiUsersObserver)
            verify(apiHelper).searchMarket(
                term = API_KEY,
                limit = QUERY_PAGE_SIZE,
                district = "chittor",
                market = "chittoor",
                offset = 0)
            verify(apiUsersObserver).onChanged(Resource.Success(viewModel.markets.value?.data!!))
            viewModel.markets.removeObserver(apiUsersObserver)
        }
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(emptyList<Market>())
                .`when`(apiHelper)
                .searchMarket(
                    term = API_KEY,
                    limit = QUERY_PAGE_SIZE,
                    district = null,
                    market = null,
                    offset = 0
                )
            viewModel.markets.observeForever(apiUsersObserver)
            verify(apiHelper).searchMarket(
                term = API_KEY,
                limit = QUERY_PAGE_SIZE,
                district = null,
                market = null,
                offset = 0)
            verify(apiUsersObserver).onChanged(Resource.Success(viewModel.markets.value?.data!!))
            viewModel.markets.removeObserver(apiUsersObserver)
        }
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message For You"
            doThrow(RuntimeException(errorMessage))
                .`when`(apiHelper)
                .searchMarket(
                    term = API_KEY,
                    limit = QUERY_PAGE_SIZE,
                    district = null,
                    market = null,
                    offset = 0)
            viewModel.markets.observeForever(apiUsersObserver)
            verify(apiHelper).searchMarket(
                term = API_KEY,
                limit = QUERY_PAGE_SIZE,
                district = null,
                market = null,
                offset = 0)
            verify(apiUsersObserver).onChanged(
                Resource.Error(
                    RuntimeException(errorMessage).toString(),
                    null
                )
            )
            viewModel.markets.removeObserver(apiUsersObserver)
        }
    }

    @After
    fun tearDown() {
    }

}