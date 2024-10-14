package com.dicoding.dicodingevent.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.database.entity.EventActiveEntity
import com.dicoding.dicodingevent.databinding.ItemUpcomingBinding

class EventUpcomingAdapter(private val onItemClick: (Int) -> Unit) :
    ListAdapter<EventActiveEntity, EventUpcomingAdapter.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            ItemUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            Log.d("EventUpcomingAdapter", "Item clicked: ${event.id}")
            onItemClick(event.id)
        }
    }

    class EventViewHolder(private val binding: ItemUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventActiveEntity) {
            binding.tvTitle.text = event.name
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error_data)
                )
                .into(binding.ivMediaCover)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventActiveEntity>() {
            override fun areItemsTheSame(
                oldItem: EventActiveEntity,
                newItem: EventActiveEntity
            ): Boolean {
                return oldItem.name == newItem.name
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: EventActiveEntity,
                newItem: EventActiveEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
