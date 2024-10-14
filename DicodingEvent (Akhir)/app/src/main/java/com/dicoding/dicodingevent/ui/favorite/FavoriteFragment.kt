package com.dicoding.dicodingevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.data.Results
import com.dicoding.dicodingevent.databinding.FragmentFavoriteBinding
import com.dicoding.dicodingevent.ui.adapter.EventFavoriteAdapter
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.dicoding.dicodingevent.ui.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FavoriteViewModel by viewModels { factory }

        val favoriteEventAdapter = EventFavoriteAdapter { eventId ->
            navigateToDetailActivity(eventId)
        }

        viewModel.favoritedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Results.Loading -> binding?.progressBar?.visibility = View.VISIBLE
                is Results.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    favoriteEventAdapter.submitList(result.data)

                    if (result.data.isNullOrEmpty()) {
                        binding?.tvEmptyMessage?.visibility = View.VISIBLE // Show empty message
                    } else {
                        binding?.tvEmptyMessage?.visibility = View.GONE // Hide empty message
                    }
                }

                is Results.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Snackbar.make(binding?.root!!, "Error: ${result.error}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        }

        binding?.finishedRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteEventAdapter
            Log.d("RecyclerView", "Adapter attached successfully")
        }

    }

    private fun navigateToDetailActivity(eventId: Int) {
        Log.d("HomeFragment", "Navigating to DetailActivity with eventId: $eventId")
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_EVENT_ID, eventId)
        startActivity(intent)
    }

}