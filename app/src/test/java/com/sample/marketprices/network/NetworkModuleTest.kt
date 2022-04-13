package com.sample.marketprices.network

import com.google.gson.Gson
import com.sample.marketprices.utils.Constants
import org.junit.Assert.*
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModuleTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: ApiService
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = Gson()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService::class.java)
    }


    @Test
    fun `get all market api test`() {
        runBlocking {
            val mockResponse = MockResponse()
            mockWebServer.enqueue(mockResponse.setBody("[]"))
            val response = apiService.searchMarket(
                term = Constants.API_KEY,
                limit = Constants.QUERY_PAGE_SIZE,
                district = "chittor",
                market = "chittoor",
                offset = 0)
            val request = mockWebServer.takeRequest()
            assertEquals("/resource/9ef84268-d588-465a-a308-a864a43d0070n",request.path)
            assertEquals(true, response.body())
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

}