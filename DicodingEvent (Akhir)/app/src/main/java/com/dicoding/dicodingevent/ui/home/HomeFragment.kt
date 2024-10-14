package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.Results
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.DailyReminderWorker
import com.dicoding.dicodingevent.ui.adapter.CarouselAdapter
import com.dicoding.dicodingevent.ui.adapter.EventFinishedAdapter
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.dicoding.dicodingevent.ui.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private var darkMode: Boolean = false
    private var notify: Boolean = false
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                val darkModeItem = menu.findItem(R.id.menu1)
                val notifyItem = menu.findItem(R.id.menu2)

                if (darkMode) {
                    darkModeItem.icon =
                        context?.let { ContextCompat.getDrawable(it, R.drawable.ic_light) }
                } else {
                    darkModeItem.icon =
                        context?.let { ContextCompat.getDrawable(it, R.drawable.ic_dark) }
                }

                if (notify) {
                    notifyItem.icon =
                        context?.let { ContextCompat.getDrawable(it, R.drawable.ic_notif_off) }
                } else {
                    notifyItem.icon =
                        context?.let { ContextCompat.getDrawable(it, R.drawable.ic_notif) }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu1 -> {
                        if (darkMode) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            viewModel.saveThemeSetting(false)
                            menuItem.icon =
                                context?.let { ContextCompat.getDrawable(it, R.drawable.ic_dark) }
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            viewModel.saveThemeSetting(true)
                            menuItem.icon =
                                context?.let { ContextCompat.getDrawable(it, R.drawable.ic_light) }
                        }
                        true
                    }

                    R.id.menu2 -> {
                        if (notify) {
                            viewModel.saveReminderSetting(false)
                            menuItem.icon =
                                context?.let { ContextCompat.getDrawable(it, R.drawable.ic_notif) }
                            scheduleCancel()
                        } else {
                            viewModel.saveReminderSetting(true)
                            menuItem.icon =
                                context?.let { ContextCompat.getDrawable(it, R.drawable.ic_notif_off) }
                            scheduleDailyReminder()
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)

        return binding?.root
    }

    private fun scheduleCancel() {
        workManager.cancelAllWorkByTag("EVENT")
    }

    private fun scheduleDailyReminder() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(
            DailyReminderWorker::class.java, 1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .addTag("EVENT")
            .build()
        workManager.enqueue(periodicWorkRequest)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workManager = WorkManager.getInstance(requireContext())

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        viewModel = viewModels<HomeViewModel> { factory }.value

        val activeEventAdapter = CarouselAdapter { eventId ->
            navigateToDetailActivity(eventId)
        }

        val finishedEventAdapter = EventFinishedAdapter { eventId ->
            navigateToDetailActivity(eventId)
        }

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            darkMode = isDarkModeActive
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        viewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderActive ->
            notify = isReminderActive
            activity?.invalidateOptionsMenu()
        }


        viewModel.activeEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Results.Loading -> binding?.progressBar?.visibility = View.VISIBLE
                is Results.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    activeEventAdapter.submitList(result.data.take(5))
                }

                is Results.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Snackbar.make(binding?.root!!, "Error: ${result.error}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Results.Loading -> binding?.progressBar?.visibility = View.VISIBLE
                is Results.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    finishedEventAdapter.submitList(result.data.take(5))
                }

                is Results.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Snackbar.make(binding?.root!!, "Error: ${result.error}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding?.carouselRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = activeEventAdapter
        }

        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedEventAdapter
            Log.d("RecyclerView", "Adapter attached successfully")
        }
    }

    private fun navigateToDetailActivity(eventId: Int) {
        Log.d("HomeFragment", "Navigating to DetailActivity with eventId: $eventId")
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_EVENT_ID, eventId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
