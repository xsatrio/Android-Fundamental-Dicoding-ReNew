package com.dicoding.dicodingevent.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentSearchBinding
import com.dicoding.dicodingevent.ui.adapter.EventSearchAdapter
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class SearchFragment : Fragment() {
    private lateinit var eventAdapter: EventSearchAdapter
    private lateinit var searchViewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchView.hide()
                    val keyword = searchView.text.toString()
                    fetchData(keyword)
                    true
                }
        }

        searchViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        searchViewModel.events.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }

        searchViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                searchViewModel.clearErrorMessage()
            }
        }

        return root
    }

    private fun fetchData(keyword: String) {
        searchViewModel.getEvents(keyword)
    }

    private fun setupRecyclerView() {
        eventAdapter = EventSearchAdapter({ selectedEvent -> navigateToDetail(selectedEvent.id) }, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = eventAdapter
    }


    private fun navigateToDetail(eventId: Int) {
        Log.d("SearchFragment", "Navigating to DetailActivity with eventId: $eventId")
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_EVENT_ID, eventId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
