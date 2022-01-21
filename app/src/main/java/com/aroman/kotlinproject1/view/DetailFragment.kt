package com.aroman.kotlinproject1.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.aroman.kotlinproject1.databinding.DetailFragmentBinding
import com.aroman.kotlinproject1.model.*
import com.aroman.kotlinproject1.viewmodel.DetailViewModel

class DetailFragment : Fragment() {

    companion object {
        fun newInstance(bundle: Bundle?): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    private val listener = Repository.OnLoadListener {

        RepositoryImpl.getWeatherFromServer()?.let { weather ->
            Log.d("WEATHERLOADED", "onLoaded: $weather")

            with(binding) {
                weatherCondition.text = weather.condition
                temperature.text = weather.temperature.toString()
                feelsLike.text = weather.feelsLike.toString()


                val request = ImageRequest.Builder(requireContext())
                    .data("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
                    .target(weatherImage)
                    .build()

                ImageLoader.Builder(requireContext())
                    .componentRegistry { add(SvgDecoder(requireContext())) }
                    .build()
                    .enqueue(request)
            }

            viewModel.saveHistory(weather)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RepositoryImpl.addLoadListener(listener)

        arguments?.getParcelable<Weather>("WEATHER_EXTRA")?.let { weather ->
            with(binding) {
                cityName.text = weather.city.name
                cityCoordinates.text = "lat: ${weather.city.lat} lon: ${weather.city.lon}"
            }

            requireActivity().startService(
                Intent(
                    requireContext(),
                    WeatherIntentService::class.java
                ).apply {
                    putExtra("WEATHER_EXTRA", weather)
                })
        }

        viewModel.getData().observe(viewLifecycleOwner){

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RepositoryImpl.removeLoadListener(listener)
        _binding = null
    }
}