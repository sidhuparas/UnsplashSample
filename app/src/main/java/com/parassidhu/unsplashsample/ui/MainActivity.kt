package com.parassidhu.unsplashsample.ui

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.parassidhu.unsplashsample.R
import com.parassidhu.unsplashsample.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private val listAdapter by lazy { ListAdapter(mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        viewModel.getPhotos()
        setupObservers()
        setupRecyclerView()
    }

    private fun setupObservers() {
        viewModel.photosResponse.observe(this) { list ->
            progress.isVisible = false
            listAdapter.addData(list)
        }
    }

    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    private fun setupRecyclerView() {
        unsplashRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = listAdapter
        }

        unsplashRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val mLayoutManager =
                    unsplashRecyclerView.layoutManager as? StaggeredGridLayoutManager ?: return

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                var firstVisibleItems: IntArray? = null

                firstVisibleItems = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItems)
                if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
                    pastVisibleItems = firstVisibleItems[0]
                }

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    if (viewModel.canRequestMore())
                        viewModel.getPhotos()
                }
            }
        })
    }
}
