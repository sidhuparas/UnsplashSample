package com.parassidhu.unsplashsample.ui

import android.content.Context
import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast
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

    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        setupObservers()
        setupRecyclerView()
    }

    private fun setupObservers() {
        viewModel.photosResponse.observe(this) { list ->
            progress.isVisible = false
            listAdapter.addData(list)
        }

        viewModel.errorLiveData.observe(this) { flag ->
            Toast.makeText(this, getString(R.string.error_text), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        unsplashRecyclerView.apply {
            layoutManager = getStaggeredLayoutManager()
            adapter = listAdapter
        }

        listAdapter.clear()
        listAdapter.addData(viewModel.getTotalList())

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

    private fun getStaggeredLayoutManager(): StaggeredGridLayoutManager {
        val display =
            (getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay

        val orientation = display.rotation

        if (orientation == Surface.ROTATION_90
            || orientation == Surface.ROTATION_270
        ) {
            return StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        } else {
            return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.clear() // Added to avoid duplicate data on activity recreation
    }
}
