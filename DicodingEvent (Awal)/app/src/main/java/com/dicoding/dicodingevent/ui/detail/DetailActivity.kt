package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    companion object {
        const val EXTRA_EVENTID = "extra_eventid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val eventID = intent.getIntExtra(EXTRA_EVENTID, -1)
        if (eventID != -1) {
            observeViewModel()
            viewModel.fetchDetailEvent(context = this, id = eventID)
        } else {
            Log.e("DetailActivity", "Invalid Event ID")
        }

        binding.seeEvent.setOnClickListener {
            val eventLink = viewModel.detailEvent.value?.link
            if (eventLink.isNullOrEmpty()) {
                Snackbar.make(binding.root, getString(R.string.title_error), Snackbar.LENGTH_LONG).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(eventLink)
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.detailEvent.observe(this) { event ->
            displayEventData(event)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.ivMediaCover.setImageResource(R.drawable.ic_error_data)
                binding.tvTitle.text = getString(R.string.title_error)
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun displayEventData(event: Event) {
        val sisaKuota = event.quota - event.registrants

        binding.tvTitle.text = event.name
        binding.tvSummary.text = event.summary
        binding.tvQuota.text = getString(R.string.quota_text, sisaKuota)
        binding.tvDate.text = event.beginTime
        binding.tvDescription.text = HtmlCompat.fromHtml(
            event.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        Glide.with(this)
            .load(event.mediaCover).listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable?>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(binding.ivMediaCover)

        supportActionBar?.title = event.name
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
