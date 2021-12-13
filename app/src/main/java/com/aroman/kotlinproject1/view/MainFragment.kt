package com.aroman.kotlinproject1.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aroman.kotlinproject1.viewmodel.MainViewModel
import com.aroman.kotlinproject1.model.Weather
import com.aroman.kotlinproject1.databinding.MainFragmentBinding
import com.aroman.kotlinproject1.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, { state -> render(state) })
        viewModel.getWeather()
    }

    private fun render(state: AppState) {
        when (state) {
            is AppState.Success -> {
                binding.loadingContainer.visibility = View.GONE
                val weather = state.weather as Weather
                binding.cityName.text = weather.city
                binding.temperature.text = weather.temperature.toString()
                binding.latitude.text = ("lat: ") + weather.latitude.toString()
                binding.longitude.text = ("long: ") + weather.longitude.toString()
            }
            is AppState.Error -> {
                binding.loadingContainer.visibility = View.VISIBLE
                Snackbar.make(binding.root, state.error.toString(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try again") { viewModel.getWeather() }.show()
            }
            is AppState.Loading -> {
                binding.loadingContainer.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}