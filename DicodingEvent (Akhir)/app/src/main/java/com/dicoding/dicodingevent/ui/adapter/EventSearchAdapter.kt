package com.dicoding.dicodingevent.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ItemEventBinding
import com.dicoding.dicodingevent.databinding.ItemFinishedBinding

class EventSearchAdapter(
    private val listener: (ListEventsItem) -> Unit,
    private val isFinishedFragment: Boolean
) : ListAdapter<ListEventsItem, EventSearchAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return if (isFinishedFragment) {
            R.layout.item_finished
        } else {
            R.layout.item_event
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = when (viewType) {
            R.layout.item_finished -> ItemFinishedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            else -> ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return MyViewHolder(binding, isFinishedFragment)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, listener)
    }

    class MyViewHolder(
        private val binding: ViewBinding,
        private val isFinishedFragment: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem, listener: (ListEventsItem) -> Unit) {
            if (isFinishedFragment && binding is ItemFinishedBinding) {
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    tvTitle.text = event.name
                    tvSummary.text = event.summary

                    Glide.with(itemView.context)
                        .load(event.mediaCover)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                ivMediaCover.setImageResource(R.drawable.ic_error_data)
                                progressBar.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable?>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility = View.GONE
                                return false
                            }
                        })
                        .into(ivMediaCover)
                }
            } else if (!isFinishedFragment && binding is ItemEventBinding) {
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    tvTitle.text = event.name
                    tvSummary.text = event.summary

                    Glide.with(itemView.context)
                        .load(event.mediaCover)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                ivMediaCover.setImageResource(R.drawable.ic_error_data)
                                progressBar.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable?>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility = View.GONE
                                return false
                            }
                        })
                        .into(ivMediaCover)
                }
            }


            itemView.setOnClickListener {
                listener(event)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
