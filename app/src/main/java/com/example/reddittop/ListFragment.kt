package com.example.reddittop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddittop.adapters.TopAdapter
import com.example.reddittop.adapters.TopLoadStateAdapter
import com.example.reddittop.dao.TopEntry
import com.example.reddittop.databinding.FragmentDetailsBinding
import com.example.reddittop.databinding.FragmentListBinding
import com.example.reddittop.viewModel.RedditViewModel
import com.example.reddittop.viewModel.RedditViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(),TopAdapter.ItemClicked {
    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: RedditViewModel
    private lateinit var topAdapter: TopAdapter

    @Inject
    lateinit var factory: RedditViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), factory).get(RedditViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentListBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setupPageLoading()
        setupPullToRefresh()
        setupObservers()
        setupActions()
    }

    private fun setupActions() {
        binding.dismissAll.setOnClickListener {
            viewModel.dismissAll()
        }
    }

    private fun setupObservers() {
        viewModel.itemRemoved.observe(requireActivity(), {
            topAdapter.notifyItemRemoved(it)
        })
    }

    private fun initRecyclerView() {
        topAdapter = TopAdapter(this)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = topAdapter.withLoadStateFooter(
            footer = TopLoadStateAdapter { topAdapter.retry() }
        )
    }

    private fun setupPageLoading() {
        lifecycleScope.launch {
            viewModel.topEntries.collectLatest { page ->
                binding.pullToRefresh.isRefreshing = false
                topAdapter.submitData(page)
            }
        }
    }

    private fun setupPullToRefresh() {
        binding.pullToRefresh.setOnRefreshListener {
            topAdapter.refresh()
        }
    }

    override fun onItemClicked(item: TopEntry) {
        viewModel.showItem(item)
    }

    override fun onItemDismissed(item: TopEntry) {
        viewModel.dismissItem(item)
    }
}