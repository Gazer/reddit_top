package com.example.reddittop

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddittop.adapters.TopAdapter
import com.example.reddittop.adapters.TopLoadStateAdapter
import com.example.reddittop.databinding.ActivityMainBinding
import com.example.reddittop.models.Children
import com.example.reddittop.viewModel.RedditViewModel
import com.example.reddittop.viewModel.RedditViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TopAdapter.ItemClicked {
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
        setupDrawer()
    }

    private fun setupDrawer() {
        if (binding.drawer != null) {
            val toggle = ActionBarDrawerToggle(
                this,
                binding.drawer,
                binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            binding.drawer?.addDrawerListener(toggle)
            toggle.syncState()
        }
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
        viewModel.currentItem.observe(this, {
            binding.test.text = it.data.title
            binding.drawer?.closeDrawers()
        })
    }

    private fun initRecyclerView() {
        topAdapter = TopAdapter(this)
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = topAdapter.withLoadStateFooter(
            footer = TopLoadStateAdapter { topAdapter.retry() }
        )
    }

    override fun onItemClicked(item: Children) {
        viewModel.showItem(item)
    }
}