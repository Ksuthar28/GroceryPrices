package com.sample.marketprices.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.marketprices.models.Market
import com.sample.marketprices.repo.MarketRepository
import com.sample.marketprices.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val repository: MarketRepository,
) : ViewModel() {

    val markets = MutableLiveData<Resource<MutableList<Market>>>()
    var pageNumber = 1

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        markets.postValue(Resource.Error("Error: ${throwable.localizedMessage}"))
    }

    fun getMarkets(
        district: String?,
        market: String?
    ) =
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            markets.postValue(Resource.Loading())
            val response = repository.getAllMarket(district, market, pageNumber)
            pageNumber++
            markets.postValue(response)
        }

    fun resetPage() {
        pageNumber = 1
    }

}