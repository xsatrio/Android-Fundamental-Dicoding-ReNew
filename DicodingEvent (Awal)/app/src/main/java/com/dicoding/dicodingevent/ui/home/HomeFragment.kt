package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.adapter.CarouselAdapter
import com.dicoding.dicodingevent.ui.adapter.EventAdapter
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

@Suppress("ReplaceGetOrSet")
class HomeFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupCarousel()

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        homeViewModel.events.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }

        homeViewModel.carouselEvents.observe(viewLifecycleOwner) { carouselEvents ->
            carouselAdapter.submitList(carouselEvents)
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                homeViewModel.clearErrorMessage()
            }
        }

        homeViewModel.getCarousel(context = requireContext())
        homeViewModel.getEvents(context = requireContext())

        return root
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter({ selectedEvent -> navigateToDetail(selectedEvent) }, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = eventAdapter
    }

    private fun setupCarousel() {
        carouselAdapter = CarouselAdapter { selectedEvent -> navigateToDetail(selectedEvent) }
        binding.carouselRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.carouselRecyclerView.adapter = carouselAdapter
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
