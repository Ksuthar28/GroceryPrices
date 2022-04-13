package com.sample.marketprices.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sample.marketprices.R
import com.sample.marketprices.adapters.MarketAdapter
import com.sample.marketprices.databinding.ActivityHomeBinding
import com.sample.marketprices.models.Market
import com.sample.marketprices.utils.CheckInternet
import com.sample.marketprices.utils.Constants
import com.sample.marketprices.utils.InfiniteScrollListener
import com.sample.marketprices.utils.MyExtension.capWords
import com.sample.marketprices.utils.MyExtension.dismissKeyboard
import com.sample.marketprices.utils.Resource
import com.sample.marketprices.viewmodel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    @Inject
    lateinit var rAdapter: MarketAdapter
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!
    private var district: String = ""
    private var market: String = ""
    private var shortByPrice: Boolean = true;

    private val viewModel: MarketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetup()
    }

    private fun initialSetup() {
        filterMarket()
        setupRecyclerView()
    }

    private fun filterMarket() {

        binding.buttonSearch.setOnClickListener {
            if (validateSearch()) search()
        }

        binding.searchDistrict.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty() && district.isNotEmpty()) search()
                return true
            }
        })
        binding.searchMarket.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty() && market.isNotEmpty()) search()
                return true
            }
        })

        binding.sortRadioGroup.setOnCheckedChangeListener { radioGroup, _ ->
            shortByPrice = radioGroup.checkedRadioButtonId == R.id.radioPrice
            rAdapter.differ.submitList(sortData(rAdapter.differ.currentList))
        }

    }

    private fun validateSearch(): Boolean {
        if (binding.searchDistrict.query.toString()
                .isEmpty() && binding.searchMarket.query.toString()
                .isEmpty()
        ) {
            Snackbar.make(
                binding.root,
                getString(R.string.search_error),
                Snackbar.LENGTH_SHORT
            ).show()
            return false
        }
        return true;
    }

    private fun search() {
        binding.root.dismissKeyboard(baseContext)
        viewModel.resetPage()
        loadData()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(baseContext)
        val infiniteScrollListener = object : InfiniteScrollListener(linearLayoutManager) {
            override fun isDataLoading(): Boolean {
                return isLoading
            }

            override fun onLoadMore() {
                if (!isLastPage) loadData()
            }
        }
        with(binding.list) {
            layoutManager = linearLayoutManager
            adapter = rAdapter
            addOnScrollListener(infiniteScrollListener)
        }
        initViewModelObservers()
    }

    /**
     * updating data to the adapter.
     * */
    private fun initViewModelObservers(
    ) {
        viewModel.markets.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        val finalList = sortData(it.toSet().toList())
                        rAdapter.differ.submitList(finalList)
                        val totalPages = finalList.size / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.pageNumber == totalPages
                        if (finalList.isEmpty())
                            showError(getString(R.string.result_not_available))
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        showError(it)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                else -> Unit
            }
        }
        loadData()
    }

    private fun loadData() {
        district = binding.searchDistrict.query.toString().capWords().trimStart().trimEnd()
        market = binding.searchMarket.query.toString().capWords().trimStart().trimEnd()
        baseContext?.let {
            if (CheckInternet.networkConnected(it))
                viewModel.getMarkets(district.ifEmpty { null }, market.ifEmpty { null })//
            else
                Snackbar.make(
                    binding.root,
                    getString(R.string.internet_not_connected),
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(
                        "Retry"
                    ) { loadData() }
                    .show()
        }
    }

    private fun sortData(list: List<Market>): List<Market> {
        return list.sortedWith(compareBy {
            if (shortByPrice) it.modalPrice.toInt() else SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).parse(it.arrivalDate)
        })
    }


    private fun showError(error: String) {
        binding.progressBar.visibility = View.GONE
        binding.emptyText.text = error
        binding.emptyText.visibility = View.VISIBLE
        rAdapter.differ.submitList(null)
    }

    var isLoading = false
    var isLastPage = false

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyText.visibility = View.GONE
        isLoading = true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.list.adapter = null
        _binding = null
    }

}