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
import com.example.reddittop.dao.TopEntry
import com.example.reddittop.databinding.ActivityMainBinding
import com.example.reddittop.models.Children
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
        setupViewModel()
        setupObserver()
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

    private fun initUI() {
        binding.toolbar.title = getString(R.string.app_name)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(RedditViewModel::class.java)
    }

    private fun setupObserver() {
        viewModel.currentItem.observe(this, {
            binding.drawer?.closeDrawers()
        })
    }
}