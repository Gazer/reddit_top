package com.example.reddittop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddittop.adapters.TopAdapter
import com.example.reddittop.adapters.TopLoadStateAdapter
import com.example.reddittop.databinding.ActivityMainBinding
import com.example.reddittop.viewModel.RedditViewModel
import com.example.reddittop.viewModel.RedditViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RedditViewModel
    private lateinit var topAdapter: TopAdapter

    @Inject
    lateinit var factory: RedditViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater, null, false).also {
            binding = it
        }.run {
            setContentView(root)
        }

        initUI()
        initRecyclerView()
        setupViewModel()
        setupObserver()
        setupPullToRefresh()
        setupPageLoading()
    }

    private fun setupPageLoading() {
        lifecycleScope.launch {
            viewModel.topEntries.collectLatest { page ->
                binding.pullToRefresh.isRefreshing = false
                topAdapter.submitData(page)
            }
        }
    }

    private fun initUI() {
        binding.toolbar.title = getString(R.string.app_name)
    }

    private fun setupPullToRefresh() {
        binding.pullToRefresh.setOnRefreshListener {
            topAdapter.refresh()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(RedditViewModel::class.java)
    }

    private fun setupObserver() {
        // TODO: binding.pullToRefresh.isRefreshing = false
    }

    private fun initRecyclerView() {
        topAdapter = TopAdapter()
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = topAdapter.withLoadStateFooter(
            footer = TopLoadStateAdapter { topAdapter.retry() }
        )
    }
}