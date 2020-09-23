package com.example.reddittop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reddittop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater, null, false).also {
            binding = it
        }.run {
            setContentView(root)
        }

        binding.toolbar.title = getString(R.string.app_name)
    }
}