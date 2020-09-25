package com.example.reddittop.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittop.dao.TopEntry
import com.example.reddittop.databinding.ItemEntryBinding
import com.example.reddittop.models.Children
import com.squareup.picasso.Picasso
import java.util.*

class TopAdapter(private val itemClicked: ItemClicked) :
    PagingDataAdapter<TopEntry, TopAdapter.ItemViewHolder>(ItemComparator) {
    interface ItemClicked {
        fun onItemClicked(item: TopEntry)
        fun onItemDismissed(item: TopEntry)
    }

    inner class ItemViewHolder(private val binding: ItemEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TopEntry) {
            binding.root.setOnClickListener {
                itemClicked.onItemClicked(item)
            }
            binding.dismiss.setOnClickListener {
                itemClicked.onItemDismissed(item)
            }
            binding.userName = item.author
            binding.title = item.title
            binding.comments =
                String.format(Locale.getDefault(), "%s comments", item.comments)

            binding.timeAgo = DateUtils.getRelativeDateTimeString(
                binding.timeAgoField.context,
                item.created.toLong() * 1000,
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

    object ItemComparator : DiffUtil.ItemCallback<TopEntry>() {
        override fun areItemsTheSame(oldItem: TopEntry, newItem: TopEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TopEntry, newItem: TopEntry): Boolean {
            return oldItem == newItem
        }
    }
}