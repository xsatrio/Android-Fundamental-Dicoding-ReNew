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
import com.dicoding.dicodingevent.database.entity.EventFinishedEntity
import com.dicoding.dicodingevent.databinding.ItemEventBinding

class EventFinishedAdapter(private val onItemClick: (Int) -> Unit) : ListAdapter<EventFinishedEntity, EventFinishedAdapter.EventViewHolder>(DIFF_CALLBACK) {

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

    class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventFinishedEntity) {
            binding.tvTitle.text = event.name
            binding.tvSummary.text = event.summary
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error_data))
                .into(binding.ivMediaCover)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventFinishedEntity>() {
            override fun areItemsTheSame(oldItem: EventFinishedEntity, newItem: EventFinishedEntity): Boolean {
                return oldItem.name == newItem.name
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: EventFinishedEntity, newItem: EventFinishedEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
