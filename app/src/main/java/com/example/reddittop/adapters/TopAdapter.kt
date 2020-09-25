package com.example.reddittop.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittop.databinding.ItemEntryBinding
import com.example.reddittop.models.Children
import com.squareup.picasso.Picasso
import java.util.*

class TopAdapter(private val itemClicked: ItemClicked) :
    PagingDataAdapter<Children, TopAdapter.ItemViewHolder>(ItemComparator) {
    interface ItemClicked {
        fun onItemClicked(item: Children)
    }

    inner class ItemViewHolder(private val binding: ItemEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Children) {
            binding.root.setOnClickListener {
                itemClicked.onItemClicked(item)
            }
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemEntryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item)
        }
    }

    object ItemComparator : DiffUtil.ItemCallback<Children>() {
        override fun areItemsTheSame(oldItem: Children, newItem: Children): Boolean {
            return oldItem.data.id == newItem.data.id
        }

        override fun areContentsTheSame(oldItem: Children, newItem: Children): Boolean {
            return oldItem == newItem
        }
    }
}