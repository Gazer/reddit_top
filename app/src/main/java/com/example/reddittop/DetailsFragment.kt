package com.example.reddittop

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.reddittop.dao.TopEntry
import com.example.reddittop.databinding.FragmentDetailsBinding
import com.example.reddittop.viewModel.RedditViewModel
import com.example.reddittop.viewModel.RedditViewModelFactory
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedOutputStream
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

    private fun showDetails(item: TopEntry) = with(binding) {
        userName = item.author
        title = item.title
        comments =
            String.format(Locale.getDefault(), "%s comments", item.comments)

        timeAgo = DateUtils.getRelativeDateTimeString(
            timeAgoField.context,
            item.created * 1000,
            DateUtils.HOUR_IN_MILLIS,
            DateUtils.DAY_IN_MILLIS,
            0
        ).toString()

        val image = item.imageUrl
        if (image != null) {
            Picasso.get().load(image).into(previewImage)
            previewImage.setOnLongClickListener {
                saveImage(item.id)
                true
            }
            previewImage.visibility = View.VISIBLE
        } else {
            previewImage.visibility = View.GONE
        }

        open.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(item.url)
            }.run {
                startActivity(this)
            }
        }

        scrollable.scrollTo(0, 0)
        container.visibility = View.VISIBLE
    }

    private fun saveImage(id: String) {
        // Quick & Dirty save
        binding.previewImage.isDrawingCacheEnabled = true
        val bitmap: Bitmap = binding.previewImage.getDrawingCache()

        addImageToGallery("reddit_$id.jpg", bitmap)
        binding.previewImage.isDrawingCacheEnabled = false

        Toast.makeText(requireContext(), "Image saved", Toast.LENGTH_SHORT).show()
    }

    private fun addImageToGallery(fileName: String, bitmap: Bitmap) {
        val values = ContentValues()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.ImageColumns.TITLE, fileName)

        val uri: Uri? = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        uri?.let {
            requireActivity().contentResolver.openOutputStream(uri)?.let { stream ->
                val outputStream = BufferedOutputStream(stream)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            }
        }
    }
}