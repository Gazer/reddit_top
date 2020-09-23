package com.example.reddittop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.reddittop.databinding.ActivityMainBinding
import com.example.reddittop.networking.RedditAPI
import com.example.reddittop.viewModel.RedditViewModel
import com.example.reddittop.viewModel.RedditViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RedditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater, null, false).also {
            binding = it
        }.run {
            setContentView(root)
        }

        binding.toolbar.title = getString(R.string.app_name)

        // TODO: Add DI later. Just testing if API works
        val factory = RedditViewModelFactory(RedditAPI())
        viewModel = ViewModelProvider(this, factory).get(RedditViewModel::class.java)

        lifecycleScope.launch {
            viewModel.loadTop()
        }
    }
}