package com.sample.marketprices.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A scroll listener for RecyclerView to load more items as you approach the end.
 */

abstract class InfiniteScrollListener(
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    private val loadMoreRunnable = Runnable { onLoadMore() }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy < 0 || isDataLoading()) return

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD) {
            recyclerView.post(loadMoreRunnable)
        }
    }

    abstract fun isDataLoading(): Boolean

    abstract fun onLoadMore()

    companion object {
        // The minimum number of items remaining before we should loading more.
        private const val VISIBLE_THRESHOLD = 3
    }
}