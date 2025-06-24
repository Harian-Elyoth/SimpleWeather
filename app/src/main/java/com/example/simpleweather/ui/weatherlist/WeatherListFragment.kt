package com.example.simpleweather.ui.weatherlist

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.R
import com.example.simpleweather.adapter.WeatherCardAdapter
import com.example.simpleweather.databinding.FragmentWeatherListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class WeatherListFragment : Fragment() {
    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WeatherCardAdapter
    private val viewModel: WeatherListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeToDelete()
        setupFloatingActionButton()
        setupPullToRefresh()
        setupMenu()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        // Refresh weather data when fragment becomes visible
        viewModel.refreshWeatherData()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        showApiKeyDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        adapter = WeatherCardAdapter()
        binding.weatherRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WeatherListFragment.adapter
        }
    }
    
    private fun setupSwipeToDelete() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val weatherCard = adapter.currentList[position]
                
                viewModel.removeCity(weatherCard.city)
                
                Snackbar.make(
                    binding.root,
                    "${weatherCard.city} removed from favorites",
                    Snackbar.LENGTH_LONG
                ).setAction("UNDO") {
                    viewModel.addCity(weatherCard.city)
                }.show()
            }
        }
        
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.weatherRecyclerView)
    }
    
    private fun setupFloatingActionButton() {
        binding.addCityFab.setOnClickListener {
            showAddCityDialog()
        }
    }
    
    private fun setupPullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshWeatherData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
    
    private fun showAddCityDialog() {
        val context = context ?: return
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_city, null)
        val cityNameEditText = dialogView.findViewById<EditText>(R.id.cityNameEditText)
        
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)
            .create()
        
        dialogView.findViewById<View>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }
        
        dialogView.findViewById<View>(R.id.addButton).setOnClickListener {
            val cityName = cityNameEditText.text.toString().trim()
            if (cityName.isNotEmpty()) {
                // Validate city name (only letters, spaces, and common punctuation)
                if (!cityName.matches(Regex("^[a-zA-Z\\s\\-',.]+$"))) {
                    cityNameEditText.error = "Please enter a valid city name"
                    return@setOnClickListener
                }
                
                // Check if API key is set
                val prefs = requireContext().getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
                val apiKey = prefs.getString("api_key", "")
                if (apiKey.isNullOrEmpty()) {
                    Snackbar.make(binding.root, "Please set your API key in settings first", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                
                // Check if city already exists
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val isCityExists = viewModel.isCityFavorite(cityName)
                        if (isCityExists) {
                            Snackbar.make(binding.root, "City '$cityName' is already in your favorites", Snackbar.LENGTH_LONG).show()
                        } else {
                            viewModel.addCity(cityName)
                            dialog.dismiss()
                        }
                    } catch (e: Exception) {
                        Snackbar.make(binding.root, "Error checking city: ${e.message}", Snackbar.LENGTH_LONG).show()
                    }
                }
            } else {
                cityNameEditText.error = "Please enter a city name"
            }
        }
        
        dialog.show()
    }
    
    private fun showApiKeyDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_api_key, null)
        val apiKeyEditText = dialogView.findViewById<EditText>(R.id.apiKeyEditText)
        val prefs = requireContext().getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        val currentKey = prefs.getString("api_key", "")
        apiKeyEditText.setText(currentKey)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<View>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.saveButton).setOnClickListener {
            val newKey = apiKeyEditText.text.toString().trim()
            if (newKey.isNotEmpty()) {
                prefs.edit().putString("api_key", newKey).apply()
                Snackbar.make(binding.root, "API key saved!", Snackbar.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                apiKeyEditText.error = "Please enter a valid API key"
            }
        }

        dialog.show()
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weatherCards.collect { weatherCards ->
                adapter.submitList(weatherCards)
                
                // Show empty state if no cities
                if (weatherCards.isEmpty()) {
                    binding.emptyStateText.visibility = View.VISIBLE
                    binding.weatherRecyclerView.visibility = View.GONE
                } else {
                    binding.emptyStateText.visibility = View.GONE
                    binding.weatherRecyclerView.visibility = View.VISIBLE
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    if (it.contains("was not found")) {
                        // Show a styled Snackbar for city not found
                        val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.snackbar_error))
                        snackbar.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                        snackbar.setAction("OK") { snackbar.dismiss() }
                        snackbar.show()
                    } else {
                        // Fallback for other errors
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                    viewModel.clearError()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 