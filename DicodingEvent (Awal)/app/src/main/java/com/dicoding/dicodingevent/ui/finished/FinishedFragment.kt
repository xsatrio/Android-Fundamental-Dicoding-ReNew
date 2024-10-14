package com.dicoding.dicodingevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.adapter.EventAdapter
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class FinishedFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var finishedViewModel: FinishedViewModel
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        finishedViewModel = ViewModelProvider(this)[FinishedViewModel::class.java]

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        finishedViewModel.events.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }

        finishedViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                finishedViewModel.clearErrorMessage()
            }
        }
        finishedViewModel.getEvents(context = requireContext())

        return root
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter({ selectedEvent -> navigateToDetail(selectedEvent) }, true)
        binding.finishedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.finishedRecyclerView.adapter = eventAdapter
    }

    private fun navigateToDetail(event: ListEventsItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_EVENTID, event.id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}