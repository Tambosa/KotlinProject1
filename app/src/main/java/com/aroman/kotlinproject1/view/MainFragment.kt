package com.aroman.kotlinproject1.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.aroman.kotlinproject1.R
import com.aroman.kotlinproject1.viewmodel.MainViewModel
import com.aroman.kotlinproject1.model.Weather
import com.aroman.kotlinproject1.databinding.MainFragmentBinding
import com.aroman.kotlinproject1.model.City
import com.aroman.kotlinproject1.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = MainAdapter()
    private var isRussian = true

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val prefs: SharedPreferences by lazy {
        requireContext().getSharedPreferences(
            "my_prefs",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.listener = MainAdapter.OnItemClick { weather ->

            val bundle = Bundle()
            bundle.putParcelable("WEATHER_EXTRA", weather)
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.main_fragment, DetailFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commit()
            }
        }
        binding.mainRecyclerView.adapter = adapter

        viewModel.getData().observe(viewLifecycleOwner, { state -> render(state) })

        when (prefs.get("START_LOCATION", true)) {
            true -> {
                viewModel.getWeatherLocalRus()
                binding.mainFab.text = resources.getText(R.string.world)
            }
            false -> {
                viewModel.getWeatherLocalWorld()
                binding.mainFab.text = resources.getText(R.string.russia)
            }
        }

        binding.mainFab.setOnClickListener {
            isRussian = !isRussian
            prefs.set("START_LOCATION", isRussian)

            when (isRussian) {
                true -> {
                    viewModel.getWeatherLocalRus()
                    binding.mainFab.text = resources.getText(R.string.world)
                }
                false -> {
                    viewModel.getWeatherLocalWorld()
                    binding.mainFab.text = resources.getText(R.string.russia)
                }
            }
        }

        binding.historyFab.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }

        binding.geoFab.setOnClickListener {
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> showLocation()
                !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                -> AlertDialog.Builder(requireActivity()).setTitle("Permission")
                    .setMessage("Get Location")
                    .setPositiveButton("Yes") { _, _ ->
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            42
                        )
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
                else -> {
                    Toast.makeText(requireActivity(), "T_T", Toast.LENGTH_SHORT).show()
                }
            }
        }

    @SuppressLint("MissingPermission")
    private fun showLocation() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            val providerNetwork = locationManager.getProvider(LocationManager.NETWORK_PROVIDER)
            providerNetwork?.let {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            getWeatherByLocation(location)
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) { //nothing
                        }
                    }
                )
            }
        } else {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?.let { location ->
                    getWeatherByLocation(location)
                }
        }
    }

    private fun getWeatherByLocation(location: Location) {
        val geocoder = Geocoder(requireActivity())
        Thread {
            try {
                val adresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                adresses[0].locality

                val bundle = Bundle()
                bundle.putParcelable(
                    "WEATHER_EXTRA",
                    Weather(City(adresses[0].locality, location.latitude, location.longitude))
                )
                activity?.supportFragmentManager?.apply {
                    beginTransaction()
                        .replace(R.id.main_fragment, DetailFragment.newInstance(bundle))
                        .addToBackStack("")
                        .commit()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_LONG).show()
            }
        }.start()
    }

    private fun render(state: AppState) {
        when (state) {
            is AppState.Success<*> -> {
                val weather = state.data as List<Weather>
                binding.loadingContainer.hide()
                adapter.setWeather(weather)
            }
            is AppState.Error -> {
                binding.loadingContainer.show()
                Snackbar.make(binding.root, state.error.toString(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try again") { viewModel.getWeatherLocalRus() }.show()
            }
            is AppState.Loading -> {
                binding.loadingContainer.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}