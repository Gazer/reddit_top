package com.example.reddittop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddittop.databinding.ActivityMainBinding
import com.example.reddittop.networking.RedditAPI
import com.example.reddittop.viewModel.RedditViewModel
import com.example.reddittop.viewModel.RedditViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RedditViewModel
    private lateinit var topAdapter: TopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater, null, false).also {
            binding = it
        }.run {
            setContentView(root)
        }

        binding.toolbar.title = getString(R.string.app_name)

        // TODO: Add DI later. Just testing if API works
        topAdapter = TopAdapter()
        binding.list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.list.adapter = topAdapter

        val factory = RedditViewModelFactory(RedditAPI())
        viewModel = ViewModelProvider(this, factory).get(RedditViewModel::class.java)

        viewModel.topEntries.observeForever {
            topAdapter.addItems(it)
        }

        lifecycleScope.launch {
            viewModel.loadTop()
        }
    }
}