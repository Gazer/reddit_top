package com.example.reddittop

import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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

        viewModel.currentItem.observe(requireActivity(), {item ->
            binding.userName = item.data.author
            binding.title = item.data.title
            binding.comments =
                String.format(Locale.getDefault(), "%s comments", item.data.num_comments)

            binding.timeAgo = DateUtils.getRelativeDateTimeString(
                binding.timeAgoField.context,
                item.data.created.toLong() * 1000,
                DateUtils.HOUR_IN_MILLIS,
                DateUtils.DAY_IN_MILLIS,
                0
            ).toString()

            val image = item.getThumbnail()
            if (image != null) {
                Picasso.get().load(image).into(binding.previewImage);
                binding.previewImage.visibility = View.VISIBLE
            } else {
                binding.previewImage.visibility = View.GONE
            }
        })
    }
}