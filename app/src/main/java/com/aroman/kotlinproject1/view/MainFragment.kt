package com.aroman.kotlinproject1.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aroman.kotlinproject1.R
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
    private val adapter = MainAdapter()
    private var isRussian = true

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
        adapter.listener = MainAdapter.OnItemClick { weather ->

            val bundle = Bundle()
            bundle.putParcelable("WEATHER_EXTRA", weather)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, DetailFragment.newInstance(bundle))
                .addToBackStack("")
                .commit()
        }
        binding.mainRecyclerView.adapter = adapter

        viewModel.getData().observe(viewLifecycleOwner, { state -> render(state) })

        viewModel.getWeatherLocalRus()

        binding.mainFab.setOnClickListener {
            isRussian = !isRussian

            if (isRussian) {
                viewModel.getWeatherLocalRus()
                binding.mainFab.text = resources.getText(R.string.world)
            } else {
                viewModel.getWeatherLocalWorld()
                binding.mainFab.text = resources.getText(R.string.russia)
            }
        }
    }

    private fun render(state: AppState) {
        when (state) {
            is AppState.Success<*> -> {
                val weather = state.data as List<Weather>
                binding.loadingContainer.visibility = View.GONE
                adapter.setWeather(weather)
            }
            is AppState.Error -> {
                binding.loadingContainer.visibility = View.VISIBLE
                Snackbar.make(binding.root, state.error.toString(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try again") { viewModel.getWeatherLocalRus() }.show()
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