package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.Results
import com.dicoding.dicodingevent.database.entity.EventDetailEntity
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding
import com.dicoding.dicodingevent.ui.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var eventDetailEntity: EventDetailEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)
        if (eventId != 0) {
            viewModel.setEventId(eventId)
        } else {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()

        binding.fabFavorite.setOnClickListener {
            val event = eventDetailEntity
            event?.let {
                val newFavoriteState = !it.isFavorited
                viewModel.toggleFavorite(it, newFavoriteState)
                binding.fabFavorite.setImageResource(
                    if (newFavoriteState) R.drawable.ic_favorite
                    else R.drawable.ic_favorite_border
                )
            } ?: Toast.makeText(this, "Event data is not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.eventDetail.observe(this) { result ->
            when (result) {
                is Results.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Results.Success -> {
                    val event = result.data
                    eventDetailEntity = event

                    val quota = event.quota.toInt() - event.registrants.toInt()
                    binding.apply{
                        progressBar.visibility = View.GONE

                        tvTitle.text = event.name
                        tvSummary.text = event.summary
                        tvQuota.text = getString(R.string.quota_text, quota)
                        tvDate.text = event.beginTime
                        tvDescription.text =
                            HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                        Glide.with(this@DetailActivity)
                            .load(event.mediaCover)
                            .into(ivMediaCover)

                        fabFavorite.visibility = View.VISIBLE
                        fabFavorite.setImageResource(
                            if (event.isFavorited) R.drawable.ic_favorite
                            else R.drawable.ic_favorite_border
                        )

                        seeEvent.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                            startActivity(intent)
                        }
                    }


                    supportActionBar?.title = event.name
                }

                is Results.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
