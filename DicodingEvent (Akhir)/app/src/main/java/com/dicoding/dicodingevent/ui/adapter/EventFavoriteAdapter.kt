package com.dicoding.dicodingevent.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.database.entity.EventDetailEntity
import com.dicoding.dicodingevent.databinding.ItemEventBinding

class EventFavoriteAdapter(private val onItemClick: (Int) -> Unit) :
    ListAdapter<EventDetailEntity, EventFavoriteAdapter.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onItemClick(event.id)
        }
    }

    class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventDetailEntity) {
            binding.tvTitle.text = event.name
            binding.tvSummary.text = event.summary
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventDetailEntity>() {
            override fun areItemsTheSame(
                oldItem: EventDetailEntity,
                newItem: EventDetailEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: EventDetailEntity,
                newItem: EventDetailEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
