package com.example.reddittop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.reddittop.dao.TopEntry
import com.example.reddittop.databinding.FragmentDetailsBinding
import com.example.reddittop.viewModel.RedditViewModel
import com.example.reddittop.viewModel.RedditViewModelFactory
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: RedditViewModel

    @Inject
    lateinit var factory: RedditViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), factory).get(RedditViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentDetailsBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentItem.observe(requireActivity(), { item ->
            if (item == null) {
                hideDetails()
            } else {
                showDetails(item)
            }
        })
    }

    private fun hideDetails() {
        binding.container.visibility = View.GONE
    }

    private fun showDetails(item: TopEntry) {
        binding.userName = item.author
        binding.title = item.title
        binding.comments =
            String.format(Locale.getDefault(), "%s comments", item.comments)

        binding.timeAgo = DateUtils.getRelativeDateTimeString(
            binding.timeAgoField.context,
            item.created * 1000,
            DateUtils.HOUR_IN_MILLIS,
            DateUtils.DAY_IN_MILLIS,
            0
        ).toString()

        val image = item.imageUrl
        if (image != null) {
            Picasso.get().load(image).into(binding.previewImage);
            binding.previewImage.visibility = View.VISIBLE
        } else {
            binding.previewImage.visibility = View.GONE
        }

        binding.open.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(item.url)
            startActivity(i)
        }

        binding.container.visibility = View.VISIBLE
    }
}